package com.yuyi.tea.service;

import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.toolkit.ImageFactory;
import com.arcsoft.face.toolkit.ImageInfo;
import com.yuyi.tea.bean.*;
import com.yuyi.tea.common.CodeMsg;
import com.yuyi.tea.dto.FaceUserInfo;
import com.yuyi.tea.enums.ErrorCodeEnum;
import com.yuyi.tea.exception.GlobalException;
import com.yuyi.tea.mapper.ClerkMapper;
import com.yuyi.tea.mapper.PhotoMapper;
import com.yuyi.tea.mapper.ShopMapper;
import com.yuyi.tea.service.interfaces.FaceEngineService;
import com.yuyi.tea.service.interfaces.UserFaceInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.List;

@Service
@Slf4j
public class ClerkService {

    public static String REDIS_CLERKS_NAME="clerks";
    public static String REDIS_POSITIONS_NAME=REDIS_CLERKS_NAME+":positions";
    public static String REDIS_CLERK_NAME=REDIS_CLERKS_NAME+":clerk";

    @Autowired
    private ClerkMapper clerkMapper;

    @Autowired
    private PhotoMapper photoMapper;

    @Autowired
    private ShopMapper shopMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    FaceEngineService faceEngineService;

    @Autowired
    UserFaceInfoService userFaceInfoService;

    //获取除管理员外的职员列表
    public List<Clerk> getAllClerks(){
        List<Clerk> clerks = clerkMapper.getAllClerks();
        for(Clerk clerk:clerks){
            if(clerk.getShop()!=null){
                clerk.getShop().setOpenHours(null);
                clerk.getShop().setPhotos(null);
                clerk.getShop().setClerks(null);
                clerk.getShop().setShopBoxes(null);
            }
            clerk.setAvatar(null);
            clerk.setPassword(null);
            clerk.setPositionAutorityFrontDetails(null);
        }
        return clerks;
    }

    /**
     * 获取职位列表
     * @return
     */
    public List<Position> getPositions() {
        boolean hasKey=redisService.exists(REDIS_POSITIONS_NAME);
        List<Position> positions=null;
        if(hasKey){
            positions= (List<Position>) redisService.get(REDIS_POSITIONS_NAME);
            log.info("从redis中获取职位列表"+positions);
        }else{
            log.info("从数据库获取职位列表");
            positions = clerkMapper.getPositions();
            log.info("将职位列表存入redis"+positions);
            redisService.set(REDIS_POSITIONS_NAME,positions);
        }
        return positions;
    }

    /**
     * 新增职员
     * @param clerk
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveClerk(Clerk clerk) {
        //密码默认为身份证后8为
        clerk.setPassword(clerk.getIdentityId().substring(clerk.getIdentityId().length()-8));
        clerkMapper.saveClerk(clerk);
        clerk.getAvatar().setClerkId(clerk.getUid());
        photoMapper.saveClerkAvatar(clerk.getAvatar());
        //提取照片人脸特征值
        Photo avatar = photoMapper.getAvatarByClerkId(clerk.getUid());
        try {
            BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(avatar.getPhoto()));
            ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);

            //人脸特征获取
            List<FaceFeature> faceFeatureList = faceEngineService.extractFaceFeature(imageInfo);
            if (faceFeatureList == null||faceFeatureList.size()==0) {
                throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
            }
            for(FaceFeature faceFeature:faceFeatureList){
                userFaceInfoService.addFace(avatar.getPhoto(), 1, clerk);
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
        }
    }

    /**
     * 失效职员
     * @param uid
     */
    @Transactional(rollbackFor = Exception.class)
    public void terminalClerk(int uid) {
        clerkMapper.terminalClerk(uid);
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+uid);
        if(hasKey){
            log.info("职员"+uid+"离职");
            redisService.remove(REDIS_CLERK_NAME+":"+uid);
        }
    }

    /**
     * 获取职员详情
     * @param uid
     * @return
     */
    public Clerk getClerk(int uid) {
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+uid);
        Clerk clerk;
        if(hasKey){
            clerk= (Clerk) redisService.get(REDIS_CLERK_NAME+":"+uid);
            log.info("从redis获取职员信息"+clerk);
        }else{
            log.info("从数据库获取职员详情");
            clerk= clerkMapper.getClerk(uid);
            clearClerk(clerk);
            log.info("将职员详情存入redis"+clerk);
            redisService.set(REDIS_CLERK_NAME+":"+uid,clerk);
        }
        return clerk;
    }

    /**
     * 将登陆返回的职员信息内不需要的去除
     * @param clerk
     */
    public static void clearClerk(Clerk clerk){
        clerk.setPassword(null);
        if(clerk.getShop()!=null) {
            clerk.getShop().setPhotos(null);
            clerk.getShop().setShopBoxes(null);
            clerk.getShop().setClerks(null);
        }
    }

    /**
     * 更新职员信息
     * @param clerk
     */
    @Transactional(rollbackFor = Exception.class)
    public Clerk updateClerk(Clerk clerk) {
        clerkMapper.updateClerk(clerk);
        Photo avatar = photoMapper.getPhotoByUid(clerk.getAvatar().getUid());
        Photo originAvatar = photoMapper.getAvatarByClerkId(clerk.getUid());
        if(originAvatar!=null&&originAvatar.getUid()!=avatar.getUid()){
            //对比当前照片与之前照片是否是同一个人
            boolean isSame=false;
            try{
                BufferedImage bufImage = ImageIO.read(new ByteArrayInputStream(avatar.getPhoto()));
                ImageInfo imageInfo = ImageFactory.bufferedImage2ImageInfo(bufImage);


                //人脸特征获取
                List<FaceFeature> faceFeatureList = faceEngineService.extractFaceFeature(imageInfo);
                if (faceFeatureList == null) {
                    throw new GlobalException(CodeMsg.NOT_SAME_FACE);
                }
                for(FaceFeature faceFeature:faceFeatureList) {
                    //人脸比对，获取比对结果
                    if(faceEngineService.compareFaceFeature(faceFeature, clerk)){
                        isSame=true;
                    }
                }
            }catch (Exception e){
                photoMapper.deletePhoto(avatar.getUid());
                throw new GlobalException(new CodeMsg(ErrorCodeEnum.NO_FACE_DETECTED));
            }
            //是同一人
            if(isSame){
                photoMapper.deletePhoto(originAvatar.getUid());
                avatar.setClerkId(clerk.getUid());
                photoMapper.saveClerkAvatar(avatar);
            }else{
                throw new GlobalException(CodeMsg.NOT_SAME_FACE);
            }
        }
        clerk = clerkMapper.getClerk(clerk.getUid());
        boolean hasKey=redisService.exists(REDIS_CLERK_NAME+":"+clerk.getUid());
        if(hasKey){
            log.info("更新redis中职员信息");
            redisService.set(REDIS_CLERK_NAME+":"+clerk.getUid(),clerk);
        }
        return clerk;
    }

    /**
     * 从缓存中获取职员信息
     * @param uid
     * @return
     */
    public Clerk getRedisClerk(int uid){
        Clerk clerk;
        boolean hasKey = redisService.exists(REDIS_CLERK_NAME+":"+uid);
        if(hasKey){
            clerk= (Clerk) redisService.get(REDIS_CLERK_NAME+":"+uid);
            log.info("从缓存获取的数据"+ clerk);
        }else{
            log.info("从数据库中获取数据");
            clerk = clerkMapper.getClerk(uid);
            redisService.set(REDIS_CLERK_NAME+":"+uid,clerk);
            log.info("数据插入缓存" + clerk);
        }
        return clerk;
    }

    public User getClerkByContact(String contact) {
        Clerk clerkByContact = clerkMapper.getClerkByContact(contact);
        return clerkByContact;
    }

}

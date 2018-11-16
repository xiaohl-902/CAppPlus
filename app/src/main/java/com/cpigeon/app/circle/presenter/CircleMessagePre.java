package com.cpigeon.app.circle.presenter;

import android.app.Activity;

import com.cpigeon.app.circle.CircleModel;
import com.cpigeon.app.circle.ui.CircleMessageDetailsNewActivity;
import com.cpigeon.app.commonstandard.model.dao.IBaseDao;
import com.cpigeon.app.commonstandard.presenter.BasePresenter;
import com.cpigeon.app.commonstandard.view.activity.BaseActivity;
import com.cpigeon.app.commonstandard.view.fragment.BaseFragment;
import com.cpigeon.app.entity.CircleMessageEntity;
import com.cpigeon.app.entity.JPushCircleEntity;
import com.cpigeon.app.utils.CpigeonData;
import com.cpigeon.app.utils.IntentBuilder;
import com.cpigeon.app.utils.Lists;
import com.cpigeon.app.utils.RxUtils;
import com.cpigeon.app.utils.StringValid;
import com.cpigeon.app.utils.http.HttpErrorException;
import com.cpigeon.app.utils.http.LogUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;


/**
 * Created by Zhu TingYu on 2018/1/18.
 */

public class CircleMessagePre extends BasePresenter {

    public static final String HOME_MESSAGE_TYPE = "HOME_MESSAGE_TYPE";

    int userId;
    public int page = 1;
    public String type;
    public int followId;
    private int isFollow; //1关注，0取消关注
    private int isThumb; //1点赞，0取消点赞
    public int previousId;
    public int messageId;
    public String commentContent;
    public int commentId = 0;
    public List<CircleMessageEntity> circleList;
    public String homeMessageType;
    public int messageHomePosition;
    public int friendsId;

    public List<JPushCircleEntity> jPushCircleEntities = Lists.newArrayList();

    //个人详情p层入口

    public CircleMessagePre(BaseFragment fragment) {
        super(fragment);
        userId = CpigeonData.getInstance().getUserId(fragment.getActivity());
        if (fragment.getArguments() != null) {
            type = fragment.getArguments().getString(IntentBuilder.KEY_TYPE);
            friendsId = fragment.getArguments().getInt(IntentBuilder.KEY_DATA);
            homeMessageType = fragment.getArguments().getString(HOME_MESSAGE_TYPE);
        }
        messageId = fragment.getActivity().getIntent().getIntExtra(IntentBuilder.KEY_DATA, 0);
    }

    //信息详情页p层入口
    public CircleMessagePre(Activity activity) {
        super(activity);
        userId = CpigeonData.getInstance().getUserId(activity);
        type = activity.getIntent().getStringExtra(IntentBuilder.KEY_TYPE);
        homeMessageType = type;
        messageId = activity.getIntent().getIntExtra(IntentBuilder.KEY_DATA, 0);
        messageHomePosition = activity.getIntent().getIntExtra(CircleMessageDetailsNewActivity.CIRCLE_MESSAGE_POSITION, 0);
    }

    @Override
    protected IBaseDao initDao() {
        return null;
    }

    public void getMessageList(Consumer<List<CircleMessageEntity>> consumer) {
        submitRequestThrowError(CircleModel.circleMessage(friendsId == 0 ? userId : friendsId, type, messageId, page, 10).map(r -> {
            if (r.status) {
                circleList = r.data;
                return circleList;
            } else return Lists.newArrayList();
        }), consumer);
    }

    public void setFollow(Consumer<String> consumer) {
        submitRequestThrowError(CircleModel.circleFollow(userId, followId, isFollow).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void setThumb(Consumer<String> consumer) {
        submitRequestThrowError(CircleModel.circleMessageThumbUp(userId, messageId, isThumb).map(r -> {
            if (r.status) {
                return r.msg;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void addComment(Consumer<CircleMessageEntity.CommentListBean> consumer) {

        if (!StringValid.isStringValid(commentContent.trim())) {
            error.onNext(getErrorString("请填写评论内容！"));
            return;
        }

        submitRequestThrowError(CircleModel.addCircleMessageComment(userId, messageId, commentContent.trim(),previousId, commentId).map(r -> {
            if (r.status) {
                return r.data;
            } else throw new HttpErrorException(r);
        }), consumer);
    }

    public void deleteComment(Consumer<String> consumer){
        submitRequestThrowError(CircleModel.deleteCircleMessageComment(userId, commentId).map(r -> {
            if(r.status){
                return r.msg;
            }else throw new HttpErrorException(r);
        }),consumer);
    }



    public void setIsFollow(boolean isFollow) {
        this.isFollow = isFollow ? 1 : 0;
    }

    public void setIsThumb(boolean isThumb) {
        this.isThumb = isThumb ? 1 : 0;
    }

    public int getUserThumbPosition(List<CircleMessageEntity.PraiseListBean> list, int userId) {
        int position = -1;
        for (int i = 0, len = list.size(); i < len; i++) {
            CircleMessageEntity.PraiseListBean praiseListBean = list.get(i);
            if (praiseListBean.getUid() == userId) {
                position = i;
                break;
            }
        }
        return position;
    }

    public List<CircleMessageEntity.CommentListBean> getDetailsComment(){
        return circleList.get(0).getCommentList().isEmpty() ? Lists.newArrayList() : circleList.get(0).getCommentList();
    }

    public List<CircleMessageEntity.PraiseListBean> getThumbs(){
        return circleList.get(0).getPraiseList().isEmpty() ? Lists.newArrayList() : circleList.get(0).getPraiseList();
    }

    int threadNumber;

    public List<Disposable> searchCircleMessage(List<CircleMessageEntity> data,  CircleMessageEntity entity, boolean isSearchOne){
        int dataBlockNumber = 100;

        if((data.size() / dataBlockNumber) == 0){
            threadNumber = 1;
        }else {
            if((data.size() % dataBlockNumber) > 0){
                threadNumber = (data.size() / dataBlockNumber) + 1;
            }else {
                threadNumber = (data.size() / dataBlockNumber);
            }
        }

        List<Disposable> disposables = Lists.newArrayList();

        for(int i = 0; i < threadNumber; i++){
            int start;
            int end;
            start = i * dataBlockNumber;
            if(data.size() - start < dataBlockNumber){
                end = data.size();
            }else {
                end = start + dataBlockNumber;
            }
            LogUtil.print("search end start: " + start+"  "+ end + " " + "data size" + data.size());
            disposables.add(searchOnThread(data, start, end, entity, isSearchOne));
        }
        return disposables;
    }

    private Disposable searchOnThread(List<CircleMessageEntity> data, int start, int end,CircleMessageEntity entity, boolean isSearchOne){
        if(isSearchOne){
            return RxUtils.runOnNewThread(o -> {
                for (int i = start; i < end ;i ++){
                    LogUtil.print("search: " + start+"  "+ end + "data size： " + data.size() + " "+ type);
                    if(entity.getMid() == data.get(i).getMid()){
                        LogUtil.print(entity);
                        if(onSearchCircleMessageListener != null){
                            onSearchCircleMessageListener.search(i);
                        }
                    }
                }
            });
        }else {
            return RxUtils.runOnNewThread(o -> {
                LogUtil.print("search: " + start+"  "+ end + "data size： " + data.size() + " "+ type);
                for (int i = start; i < end ;i ++){
                    if(entity.getUid() == data.get(i).getUid()){
                        LogUtil.print(entity);
                        onSearchCircleMessageListener.search(i);
                    }
                }
            });
        }

    }

    public interface OnSearchCircleMessageListener{
        void search(int position);
    }

    private OnSearchCircleMessageListener onSearchCircleMessageListener;

    public void setOnSearchCircleMessageLisenter(OnSearchCircleMessageListener onSearchCircleMessageLisenter) {
        this.onSearchCircleMessageListener = onSearchCircleMessageLisenter;
    }

    public void addJPushCircleEntity(JPushCircleEntity newMessage){
        boolean isHave = false;
        for (JPushCircleEntity entity: jPushCircleEntities ) {
            if(entity.getPushId().equals(newMessage.getPushId())){
                isHave = true;
            }
        }

        if(!isHave){
            jPushCircleEntities.add(newMessage);
        }
    }

}

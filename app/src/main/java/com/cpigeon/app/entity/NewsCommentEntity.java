package com.cpigeon.app.entity;

import android.content.Context;

import com.cpigeon.app.utils.CpigeonData;

import java.util.List;

/**
 * Created by Zhu TingYu on 2018/1/11.
 */

public class NewsCommentEntity extends SnsEntity {
    public String cid;//回复id
    public String id;//评论ID
    public String userid;//评论者会员ID
    public String nicheng;//昵称
    public long time;//评论时间
    public String content;//评论内容
    public int dianzan;//点赞次数
    public List<NewsCommentEntity> reply;//回复内容列表{"nicheng":"回复人昵称","content":"回复内容"}
    public int replycount;//回复次数
    public String headurl;
    public boolean isreply;
    public boolean isExpand = false;

    public boolean icCanComment(Context context){
        return Integer.valueOf(userid).equals(CpigeonData.getInstance().getUserId(context));
    }

}

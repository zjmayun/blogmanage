package com.boot.pojo.es;

import com.boot.pojo.Blog;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldIndex;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by sunshine on 2018/10/15.
 */
@Document(indexName = "blog",type = "blog")
@XmlRootElement//将mediatype转化为xml格式
public class EsBlog implements Serializable{

    private static final Long serialVersionUID=1L;

    @Id
    private String id;
    @Field(index = FieldIndex.not_analyzed)
    private Long blogId;
    private String title;
    private String summary;
    private String content;
    @Field(index = FieldIndex.not_analyzed)//不参与全文检索
    private String username;
    @Field(index = FieldIndex.not_analyzed)
    private String avatar;
    @Field(index = FieldIndex.not_analyzed)
    private Timestamp createTime;
    @Field(index = FieldIndex.not_analyzed)
    private Long readSize=0L;
    @Field(index = FieldIndex.not_analyzed)
    private Long commentSize=0L;
    @Field(index = FieldIndex.not_analyzed)
    private Long voteSize=0L;
    private String tags;

    protected EsBlog(){}

    public EsBlog(String title,String summary){
        this.title=title;
        this.summary=summary;
    }

    public EsBlog(Long blogId, String title, String summary, String content, String username, String avatar, Timestamp timestamp, Long readSize, Long commentSize, Long voteSize, String tags) {
        this.blogId = blogId;
        this.title = title;
        this.summary = summary;
        this.content = content;
        this.username = username;
        this.avatar = avatar;
        this.readSize = readSize;
        this.commentSize = commentSize;
        this.voteSize = voteSize;
        this.tags = tags;
    }

    public void update(Blog blog){
        this.blogId=blog.getId();
        this.avatar=blog.getUser().getAvatar();
        this.commentSize=blog.getComments();
        this.readSize=blog.getReading();
        this.title=blog.getTitle();
        this.summary=blog.getSummary();
        this.content=blog.getContent();
        this.tags=blog.getTags();
        this.voteSize=blog.getVotes();
        this.username=blog.getUser().getUsername();
    }

    public EsBlog(Blog blog){
        this.blogId=blog.getId();
        this.avatar=blog.getUser().getAvatar();
        this.commentSize=blog.getComments();
        this.readSize=blog.getReading();
        this.title=blog.getTitle();
        this.summary=blog.getSummary();
        this.content=blog.getContent();
        this.tags=blog.getTags();
        this.voteSize=blog.getVotes();
        this.username=blog.getUser().getUsername();
        this.createTime=blog.getCreateTime();
    }

    public static Long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getBlogId() {
        return blogId;
    }

    public void setBlogId(Long blogId) {
        this.blogId = blogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Long getReadSize() {
        return readSize;
    }

    public void setReadSize(Long readSize) {
        this.readSize = readSize;
    }

    public Long getCommentSize() {
        return commentSize;
    }

    public void setCommentSize(Long commentSize) {
        this.commentSize = commentSize;
    }

    public Long getVoteSize() {
        return voteSize;
    }

    public void setVoteSize(Long voteSize) {
        this.voteSize = voteSize;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "EsBlog{" +
                "id='" + id + '\'' +
                ", blogId=" + blogId +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                ", createTime=" + createTime +
                ", readSize=" + readSize +
                ", commentSize=" + commentSize +
                ", voteSize=" + voteSize +
                ", tags='" + tags + '\'' +
                '}';
    }
}

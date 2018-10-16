package com.boot.pojo;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.github.rjeschke.txtmark.Processor;
import org.springframework.data.elasticsearch.annotations.Document;


/**
 * blog的主题类，这个模块超级重要
 * Created by sunshine on 2018/9/29.
 */
@Entity
@Document(indexName = "blog",type = "blog")
public class Blog implements Serializable {

    private static final Long serialVersionUID=1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长
    private Long id;

    @NotEmpty(message = "标题不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String title;

    @NotEmpty(message = "摘要不能为空")
    @Size(min=2)
    @Column(nullable = false)
    private String summary;

    @Lob//大对象，映射mysql中的Long Test类型
    @Basic(fetch = FetchType.LAZY)//懒加载
    @Size(min=2)
    @NotEmpty(message = "内容不能为空")
    @Column(nullable = false)
    private String content;

    @Lob//大对象，映射mysql中的Long Test类型
    @Basic(fetch = FetchType.LAZY)//懒加载
    @Size(min=2)
    @NotEmpty(message = "内容不能为空")
    @Column(nullable = false)
    private String htmlContent;//将md内容的数据转换为html格式

    @Column(nullable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Timestamp createTime;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinTable(name = "blog_comment",joinColumns = @JoinColumn(name = "blog_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "comment_id",referencedColumnName = "id"))
    private List<Comment> commentList;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinTable(name="blog_vote",joinColumns = @JoinColumn(name="blog_id",referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name="vote_id",referencedColumnName = "id"))
    private List<Vote> voteList;

    @OneToOne(cascade = CascadeType.DETACH,fetch = FetchType.LAZY)
    @JoinColumn(name="catalog_id")
    private Catalog catalog;

    @Column(name = "reading")
    private Long reading=0L;

    @Column(name = "comments")
    private Long comments=0L;

    @Column(name = "votes")
    private Long votes=0L;

    @Column(name = "tags",length = 100)
    private String tags;

    protected Blog(){}

    public Blog(String title, String summary, String content) {
        this.title = title;
        this.summary = summary;
        this.content = content;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        this.htmlContent= Processor.process(content);
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getReading() {
        return reading;
    }

    public void setReading(Long reading) {
        this.reading = reading;
    }

    public Long getComments() {
        return comments;
    }

    public void setComments(Long comments) {
        this.comments = comments;
    }

    public Long getVotes() {
        return votes;
    }

    public void setVotes(Long votes) {
        this.votes = votes;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        return "Blog{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", summary='" + summary + '\'' +
                ", content='" + content + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", createTime=" + createTime +
                ", user=" + user +
                ", reading=" + reading +
                ", comments=" + comments +
                ", votes=" + votes +
                ", tags='" + tags + '\'' +
                '}';
    }

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
        this.comments=Long.valueOf(this.commentList.size());
    }

    public List<Vote> getVoteList() {
        return voteList;
    }

    /**
     * 需要进行size的更新
     * @param voteList
     */
    public void setVoteList(List<Vote> voteList) {
        this.voteList = voteList;
        this.votes=Long.valueOf(voteList.size());
    }

    /**
     * 添加评论，将最新评论数进行更新
     * @param comment
     */
    public void addComment(Comment comment){
        this.commentList.add(comment);
        this.comments=Long.valueOf(this.commentList.size());
    }

    /**
     * 删除评论，同样更新评论数
     * @param commentId
     */
    public void removeComment(Long commentId){
        for(int i=0;i<this.commentList.size();i++){
            if(this.commentList.get(i).getId()==commentId){
                this.commentList.remove(i);
                break;
            }
        }
        this.comments=Long.valueOf(this.commentList.size());
    }

    /**
     * 给博客进行点赞
     * @param vote
     */
    public boolean addVotes(Vote vote){
        boolean isExists=false;
        for(int i=0;i<voteList.size();i++){
            if(voteList.get(i).getUser().getId()==vote.getUser().getId()){
                isExists=true;
                break;
            }
        }

        if(!isExists){
            voteList.add(vote);
            this.votes=Long.valueOf(this.voteList.size());
        }
        return isExists;
    }

    /**
     * 取消点赞
     * @param voteId
     */
    public void removeVote(Long voteId){
        for(int i=0;i<voteList.size();i++){
           if(voteList.get(i).getId()==voteId){
               voteList.remove(i);
               break;
           }
        }
        this.votes=Long.valueOf(this.voteList.size());
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }
}

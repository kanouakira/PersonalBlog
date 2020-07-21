<template>
  <el-card>
    <el-page-header @back="goBack" :content="msg.title" style="margin: 20px 0; font-weight: 800;">
    </el-page-header>
    <div>
      <!-- 作者信息 发表时间 评论数 以及 浏览量等信息 -->
      <div v-if="msg" style="padding-top: 30px; color: lightseagreen">
        <el-tooltip class="item" effect="dark" content="作者" placement="top">
          <span>{{msg.user.name ? msg.user.name : msg.user.username}}</span>
        </el-tooltip>
        <el-divider direction="vertical"></el-divider>
        <span><i class="el-icon-time"></i> {{$moment(msg.created).format('lll')}} </span>
        <el-divider direction="vertical"></el-divider>
        <span><i class="el-icon-s-comment"></i> {{msg.comments.length}}</span>
        <span style="float: right;"><i class="el-icon-view"></i> {{msg.views}}</span>
      </div>
      <el-divider></el-divider>
      <!-- 摘要 -->
      <p><strong>摘要:</strong>{{msg.summary}}</p>
      <el-divider><i class="el-icon-edit"></i></el-divider>
      <!-- 正文 -->
      <MarkdownPreview :initialValue="msg.body"/>
      <!-- 评论区 -->
      <el-divider v-if="msg" content-position="left">共{{msg.comments.length}}条评论</el-divider>
      <div v-for="(comment, index) in msg.comments" :key="index">
        <div v-if="comment.commentLevel == 1 && comment.status == 0" class="comment-item" style="border: lightgray 1px solid;margin-bottom: 10px; padding-left: 10px;">
          <p>
            <strong v-text="comment.name ? comment.name : comment.username"/>
            :<MarkdownPreview :initialValue="comment.body"/>
          </p>
          <p>
            <el-link :underline="false">{{$moment(comment.created).fromNow()}}</el-link>
            <el-button size="mini" class="comment-reply-link" v-on:click="changeCommentForm(comment)" style="float: right; margin-right: 10px;"><i class="el-icon-edit-outline"></i>评论</el-button>
            <el-button size="mini" @click="editComment(comment)" style="float: right; margin-right: 5px;" v-if="sharedState.user && sharedState.user.id == comment.userId"><i class="el-icon-edit-outline"></i>编辑</el-button>
          </p>

          <div v-for="(childComment, index) in comment.childComments" v-if="childComment.status == 0" :key="index" class="comment-item childComment" style="border: lightgray 1px solid; padding-left: 10px;">
            <p>
              <strong v-text="childComment.name ? childComment.name : childComment.username" />
              <span v-if="childComment.replyUsername"> 回复 <strong v-text="childComment.replyName ? childComment.replyName : childComment.replyUsername"/></span>
              :<MarkdownPreview :initialValue="childComment.body"/>
            </p>
            <p>
              <el-link :underline="false">{{$moment(childComment.created).fromNow()}}</el-link>
              <el-button size="mini" class="comment-reply-link" v-on:click="changeCommentForm(childComment)" style="float: right; margin-right: 5px;"><i class="el-icon-s-comment"></i>评论</el-button>
              <el-button size="mini" @click="editComment(childComment)" style="float: right; margin-right: 5px;" v-if="sharedState.user && sharedState.user.id == childComment.userId"><i class="el-icon-edit-outline"></i>编辑</el-button>
            </p>
          </div>
        </div>
      </div>
      <!-- 发表评论 -->
      <el-divider id="comment-form" content-position="left">发表评论</el-divider>
      <!-- Add Comment Form -->
      <el-form id="addCommentForm" ref="commentForm" :model="commentForm" :rules="rules" label-width="20px">
        <el-row v-if="sharedState.is_authenticated">
          <el-col :span="2" style="text-align: right;">
            <el-avatar @error="errorHandler">
              <img :src="sharedState.user.avatar"/>
            </el-avatar>
          </el-col>
          <el-col :span="22">
            <el-form-item prop="body">
              <el-input id="commentFormBody" type="textarea" v-model="commentForm.body" placeholder="写下你的评论..."></el-input>
              <!-- <Markdown id="commentFormBody" v-model="commentForm.body" :height="300" :toolbars="toolbars"/> -->
            </el-form-item>
          </el-col>
          <el-col :span="20" :offset="2">
            <el-form-item size="mini">
              <el-button type="primary" @click="onSubmitAddComment('commentForm')">确认</el-button>
              <el-button @click="onResetAddComment('commentForm')">取消</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <div v-else>
          <el-select @change="selectHandler" v-model="selectValue" placeholder="发表评论前，请先登录 ...">
            <el-option
              v-for="item in options"
              :key="item.value"
              :label="item.label"
              :value="item.value">
            </el-option>
          </el-select>
        </div>
      </el-form>
      <!-- DialogEditCommentForm -->
      <el-dialog title="编辑评论" :visible.sync="editFormVisible" width="80%">
        <el-form :model="editCommentForm">
          <el-form-item>
            <el-input type="textarea" v-model="editCommentForm.body" placeholder="写下你的评论..."></el-input>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button size="mini" @click="editFormVisible = false">取 消</el-button>
          <el-button size="mini" type="primary" @click="onSubmitEditComment">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </el-card>
</template>

<script>
import store from '../store.js'
import Markdown from 'vue-meditor'
import { MarkdownPreview } from 'vue-meditor'
export default {
  name: 'Post',
  data() {
    return {
      sharedState:store.state,
      editFormVisible:false,
      msg: '',
      commentForm:{
        body:'',
        commentLevel: 1,
        reply_comment_id: null,
        parent_comment_id: null,
      },
      editCommentForm:{
        id: '',
        body:'',
      },
      rules: {
        body: [
          { required: true, message: '评论不能为空', trigger: 'blur' },
          // { min: 3, max: 5, message: '长度在 3 到 5 个字符', trigger: 'blur' }
        ],
      },
      options: [
        {
          value: '站内账号',
          label: '站内账号',
        },
        {
          value: '选项2',
          label: 'Github',
        },
        {
          value: '选项3',
          label: 'Facebook',
        },
        {
          value: '选项4',
          label: '微信',
         },
      ],
      selectValue:'',
      // toolbars:{
      //   'strong':false,
      //   'italic':false,
      //   'overline':false,
      //   'h1':false,
      //   'h2':false,
      //   'h3':false,
      //   'hr':false,
      //   'quote':false,
      //   'theme':false,
      //   'preview':false,
      //   'code':false,
      // }
    }
  },
  components:{
    Markdown,
    MarkdownPreview,
  },
  methods: {
    getPost(){
      const path = `/posts/${this.$route.params.id}`
      this.$axios.get(path)
        .then((res) => {
          console.log(res.data.data)
          this.msg = res.data.data
          // this.$moment.format()
        })
        .catch((error) => {
          // eslint-disable-next-line
          console.error(error);
        })
    },
    // 发表评论
    onSubmitAddComment(formName){
      // 通知
      // const h = this.$createElement;
      // this.$notify({
      //   title: '发表评论',
      //   message: h('i', { style: 'color: teal'}, '这是发表评论这是发表评论这是发表评论这是发表评论这是发表评论')
      // });

      // 提示
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let payload = {
            // 'userId': this.sharedState.user.id,
            'postId': this.$route.params.id,
            'body': this.commentForm.body,
            'commentLevel': this.commentForm.commentLevel,
            'parentCommentId': this.commentForm.parent_comment_id,
            'replyCommentId': this.commentForm.reply_comment_id
          }
          const path = `/comments`
          this.$axios.post(path,payload)
            .then((res) => {
              if(res.status == 200){
                this.$message({
                  message: "发表评论成功",
                  type: 'success'
                });
                this.onResetAddComment('commentForm')
                this.getPost()
              }else{
                this.$message.error('发表评论失败');
              }
            })
            .catch((error) => {
              console.error(error)
            })
        } else {
          return false;
        }
      });
    },
    // 取消评论
    onResetAddComment(formName){
      this.$refs[formName].resetFields();
      // 评论框回复原位
      $('#addCommentForm').removeClass('childComment')
      $('#comment-form').after($('#addCommentForm'))
      this.commentForm.commentLevel = 1
      this.commentForm.reply_comment_id = null
      this.commentForm.parent_comment_id = null
    },
    // 返回之前页面
    goBack(){
      this.$router.back()
    },
    changeCommentForm(comment){
      // 用户需要先登录
      if (!this.sharedState.is_authenticated) {
        this.$message.error('您需要先登录才能回复评论 ...');
        this.$router.replace({
          path: '/login',
          query: { redirect: this.$route.path}
        })
      }
      this.commentForm.commentLevel = 2
      this.commentForm.reply_comment_id = comment.id
      this.commentForm.parent_comment_id = comment.parentCommentId ? comment.parentCommentId : comment.id
    },
    selectHandler(val){
      console.log(val)
      switch(val){
        case "站内账号":
          this.$router.push({path:`/login`, query: { redirect: this.$route.fullPath }})
          break;
      }
    },
    errorHandler() {
      return true
    },
    // 打开编辑评论的dialog
    editComment(comment){
      this.editFormVisible = true
      this.editCommentForm = JSON.parse(JSON.stringify(comment))
    },
    // 提交修改评论
    onSubmitEditComment(){
      const path = `/comments/${this.editCommentForm.id}`
      let payload = {
        'body': this.editCommentForm.body,
        'userId': this.editCommentForm.userId
      }
      this.$axios.put(path, payload)
        .then((res) => {
          if(res.data.status == 200){
            this.$message({
              message: "编辑评论成功",
              type: 'success'
            });
            this.getPost()
            this.editFormVisible = false
          }else{
            this.$message.error('编辑评论失败');
          }
        })
        .catch((error) => {
          console.error(error)
        })
    }
  },
  created () {
    this.getPost()
    // 点击回复评论链接后，移动并显示评论表单
    $(document).ready(function() {
      $('body').on('click', '.comment-reply-link', function() {
        // 点击回复链接的这个评论
        var $comment = $(this).closest('.comment-item');
        // 把评论框添加到要回复的评论下面
        $comment.after($('#addCommentForm'));
        // 如果是二级评论，评论框要向右缩进
        if ($comment.hasClass('childComment')) {
          $('#addCommentForm').addClass('childComment')
        } else {
          $('#addCommentForm').removeClass('childComment')
        }
        // 光标定位到评论框中
        $('#commentFormBody').focus()
      })
    })
  },
  // 当路由变化后(比如变更查询参数 page 和 per_page)重新加载数据
  beforeRouteUpdate (to, from, next) {
    next()
    this.getPost()
  }
}
</script>


<style>
  .el-row {
    margin-bottom: 20px;
    &:last-child {
      margin-bottom: 0;
    }
  }
  .el-col {
    border-radius: 4px;
  }
  .bg-purple-dark {
    background: #99a9bf;
  }
  .bg-purple {
    background: #d3dce6;
  }
  .bg-purple-light {
    background: #e5e9f2;
  }
  .grid-content {
    border-radius: 4px;
    min-height: 36px;
  }
  .row-bg {
    padding: 10px 0;
    background-color: #f9fafc;
  }
  /* 清除必填样式的*符号 */
  .el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap>.el-form-item__label:before, .el-form-item.is-required:not(.is-no-asterisk)>.el-form-item__label:before{
    content:"";
  }
</style>

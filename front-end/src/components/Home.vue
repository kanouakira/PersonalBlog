<template>
  <div>
    <div v-for="(post, index) in posts" style="margin-bottom: 10px;">
      <el-card class="box-card" shadow="hover" @click.native="redirectToDetail(post.id, post.views)">
        <div slot="header" class="clearfix">
          <span style="font-size: 20px;">{{post.title}}</span>
          <el-tag v-for="(tag, index) in post.tags" :key="index" type="success" style="margin:0 2px;" size="small">
            {{tag.name}}
          </el-tag>
          <span style="float: right; color: lightblue">
            <i class="el-icon-view"></i> {{post.views}}
            <i class="el-icon-s-comment"></i> {{post.comments.length}}
          </span>
        </div>
        <div class="text item" style="font-size: 14px;">
          {{post.summary}}
          <p style="float: right;">
            <el-link type="info" disabled>{{post.user.name ? post.user.name : post.user.username}} · {{$moment(post.created).format('lll')}}</el-link>
          </p>
        </div>
      </el-card>
    </div>
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page.sync="currentPage"
      :page-sizes="[5, 10, 15, 20]"
      :page-size="pageSize"
      layout="total,sizes, prev, pager, next"
      :total="total"
      style="text-align: center;">
    </el-pagination>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        currentDate: new Date(),
        posts:'',
        currentPage:1,
        pageSize:10,
        total:100,
      };
    },
    methods:{
      // 加载文章
      getPosts () {
        let page = 1
        let per_page = 10
        let tag = ''
        if (typeof this.currentPage != 'undefined') {
          page = this.currentPage
        }
        if (typeof this.pageSize != 'undefined') {
          per_page = this.pageSize
        }
        if (typeof this.$route.query.tag != 'undefined') {
          tag = this.$route.query.tag
        }
        const path = `/posts/?page=${page}&per_page=${per_page}&tag=${tag}`
        this.$axios.get(path)
          .then((res) => {
            this.total = res.data.data.total
            this.pageSize = res.data.data.per_page
            this.posts = res.data.data.data;
          })
          .catch((error) => {
            console.error(error);
          })
      },
      addPostViews(post_id, views) {
        const path = `/posts/${post_id}/addViews`
        this.$axios.put(path)
          .then((res) => {
            // console.log(res)
          })
          .catch((error) => {
            console.error(error);
          })
      },
      handleSizeChange(val) {
        // console.log(`每页 ${val} 条`);
        this.pageSize = val
        this.getPosts()
      },
      handleCurrentChange(val) {
        // console.log(`当前页: ${val}`);
        this.currentPage = val
        this.getPosts()
      },
      redirectToDetail(id, views){
        this.addPostViews(id, views)
        this.$router.push({path:`/post/${id}`})
      }
    },
    created () {
      this.getPosts()
    },
    // 当路由变化后(比如变更查询参数 tag)重新加载数据
    beforeRouteUpdate (to, from, next) {
      next()
      this.getPosts()
    },
  }
</script>

<style>
  .time {
    font-size: 13px;
    color: #999;
  }

  .bottom {
    margin-top: 13px;
    line-height: 12px;
  }

  .button {
    padding: 0;
    float: right;
  }

  .image {
    width: 100%;
    display: block;
  }

  .clearfix:before,
  .clearfix:after {
      display: table;
      content: "";
  }

  .clearfix:after {
      clear: both
  }
</style>

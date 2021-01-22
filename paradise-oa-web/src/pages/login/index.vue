<template>
  <div class="login-container">
    <el-form ref="formData" :model="formData" :rules="loginRules" class="login-form" auto-complete="on" label-position="left">
      <div class="title-container">
        <h3 class="title" v-text="$t('login.title')">{{ $t('login.title') }}</h3>
      </div>
      <el-form-item prop="account">
        <el-input v-model="formData.account" :placeholder="$t('login.account')" name="account" auto-complete="off" prefix-icon="el-icon-user">
          <!-- <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" /> -->
        </el-input>
      </el-form-item>

      <el-form-item prop="password">
        <el-input
          v-model="formData.password"
          :placeholder="$t('login.password')"
          name="password"
          auto-complete="off"
          @keyup.enter.native="handleLogin"
          prefix-icon="el-icon-lock"
          show-password
        >
          <!-- <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" /> -->
        </el-input>
      </el-form-item>

      <el-form-item prop="code" v-if="$store.getters.config.USE_CAPTCHA">
        <el-input v-model="formData.code" auto-complete="off" :placeholder="$t('login.code')" style="width: 66%" @keyup.enter.native="handleLogin">
          <svg-icon slot="prefix" icon-class="security" class="el-input__icon input-icon" />
        </el-input>
        <div class="login-code"><img :src="verifyImageUrl" @click="getCaptcha" /></div>
      </el-form-item>
      <el-button :loading="loading" type="primary" style="width:100%;margin-bottom:30px;" @click.native.prevent="handleLogin">{{ $t('login.logIn') }}</el-button>
    </el-form>
  </div>
</template>
<script>
import { validateLogin } from '@utils/validate';
import { langSelect } from '@is';
import service from '@utils/service';
import CryptoUtils from '@utils/CryptoUtils';

export default {
  data() {
    return {
      formData: {
        account: '',
        password: '',
        code: ''
      },
      loading: false,
      showDialog: false,
      redirect: undefined,
      verifyImageUrl: '',
      loginRules: {
        account: [{ required: true, trigger: 'blur', msg: '帐号不能为空', validator: this.checkLogin }],
        password: [{ required: true, trigger: 'blur', msg: '密码不能为空', validator: this.checkLogin }],
        code: [{ trigger: 'blur', msg: '验证码不能为空', validator: this.checkCaptcha }]
      }
    };
  },
  components: {
    langSelect
  },
  created() {
    this.getCaptcha();
    // 手动清除vuex中存储的数据,该方法不好.若是可以,使用store的方法在跳到login页面的时候全部一个一个重置
    let routes = this.$store.getters.routes;
    if (routes !== undefined && routes && routes !== 'undefined' && routes.length > 0) {
      window.location.reload();
    }
  },
  methods: {
    // 检查用户名和密码
    checkLogin(rule, value, callback) {
      if (!validateLogin(value)) {
        return callback(new Error(rule.msg));
      } else {
        callback();
      }
    },
    // 检查验证码
    checkCaptcha(rule, value, callback) {
      if (this.$store.getters.config.USE_CAPTCHA) {
        if (!validateLogin(value)) {
          return callback(new Error(rule.msg));
        } else {
          callback();
        }
      } else {
        callback();
      }
    },
    // 刷新验证码
    getCaptcha() {
      if (this.$store.getters.config.USE_CAPTCHA) {
        this.verifyImageUrl = process.env.API_ROOT + 'common/getCaptcha?source=W&' + Math.random();
      }
    },
    // 登录
    handleLogin() {
      this.$refs.formData.validate(valid => {
        if (valid) {
          this.loading = true;
          let params = { account: this.formData.account, password: CryptoUtils.AESEncode(this.formData.password + '_' + new Date().getTime()) };
          this.$store.dispatch('LOGIN', params).then(
            resp => {
              if (resp === 1) {
                this.$nextTick(() => {
                  this.$router.push('/');
                  this.loading = false;
                });
              } else {
                this.$message(resp.msg);
              }
            },
            () => {
              this.loading = false;
            }
          );
        } else {
          this.loading = false;
          return false;
        }
      });
    }
  }
};
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
@import './login.scss';
</style>

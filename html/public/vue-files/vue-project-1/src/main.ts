import Vue from 'vue'
import App from './App.vue'
//import VueRouter from 'vue-router'
import router from './router'// loads from src/router/index.js
import axios from 'axios'
import VueAxios from 'vue-axios'
//import Vuex from 'vuex'



Vue.config.productionTip = false
let host = 'localhost'; //'192.168.1.16';//window.location.hostname;
let port = 8081; //window.location.port;

Vue.prototype.$api_base_url = `${host}:8080`;
Vue.prototype.$base_url = "${host}:${port}";
Vue.config.silent = true;

new Vue({
  router: router,
  render: h => h(App),
}).$mount('#app')




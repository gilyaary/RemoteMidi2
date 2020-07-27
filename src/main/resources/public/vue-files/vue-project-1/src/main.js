import Vue from 'vue'
import App from './App.vue'
//import VueRouter from 'vue-router'
import router from './router'// loads from src/router/index.js
//import axios from 'axios'
//import VueAxios from 'vue-axios'
//import Vuex from 'vuex'
 
Vue.config.productionTip = false

new Vue({
  router: router,
  render: h => h(App),
}).$mount('#app')




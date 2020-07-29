import Vue from 'vue'
import VueRouter from  'vue-router'
import Home from './views/Home.vue'
import Files from './views/Files.vue'
import Tracks from './views/Tracks.vue'
//import VueWebsocket from "vue-websocket";

//Vue.use(VueWebsocket, "ws://localhost:8080");
Vue.use(VueRouter);

const routes = [
  {
    path: "/", //this will be refered to from the HTML template 'to' property
    name: "home",
    component: Home //this referes to line 3
  },
  {
    path: '/files',
    name: 'files',
    component: Files //this referes to line 4
  },
  {
    path: '/tracks',
    name: 'tracks',
    component: Tracks  //this referes to line 5
  },


]

const router = new VueRouter({
  mode: 'history',
  routes
})

/*
router.beforeEach((to,from,next) => {
    alert('router.BefreEach called with: ' + to + ', ' + from + ',' + next);
})
*/

export default router
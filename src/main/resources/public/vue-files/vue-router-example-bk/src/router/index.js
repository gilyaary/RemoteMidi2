import Vue from 'vue'
import VueRouter from  'vue-router'
import Home from './views/Home.vue'
import About from './views/About.vue'
import Contact from './views/Contact.vue'
import HelloWorld from '../components/HelloWorld.vue'

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "home",
    component: Home
  },
  {
    path: '/about',
    name: 'about',
    component: About
  },
  {
    path: '/contact',
    name: 'contact',
    component: Contact
  },
  {
    path: '/hello',
    name: 'hello',
    component: HelloWorld
  }


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
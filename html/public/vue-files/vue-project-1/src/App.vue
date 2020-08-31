<template>
  <div id="app">
    <nav>
      <!-- the 'to' property is bound to the 'path' property in router/index.js-->
      <router-link to="/"><button class="menu">Home</button></router-link>
      <router-link to="/files"><button class="menu">Files</button></router-link>
      <router-link to="/tracks"><button class="menu">Tracks</button></router-link>
    </nav>
    <router-view></router-view>
    

  </div>
</template>

<script lang="ts">
import { Component, Vue } from 'vue-property-decorator';
import HelloWorld from './components/HelloWorld.vue';

@Component({
  components: {
    HelloWorld,
  },
})
export default class App extends Vue {}
</script>

<style>
.menu{
  margin-right: 30px;
  font-size: 30px;
}

</style>

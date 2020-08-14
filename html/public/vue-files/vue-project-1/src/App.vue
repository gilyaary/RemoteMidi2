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

<script>
export default {
  name: 'App',
  components: {
      
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.menu{
  margin-right: 30px;
  font-size: 30px;

}
</style>

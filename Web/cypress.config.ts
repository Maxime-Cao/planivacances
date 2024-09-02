import { defineConfig } from "cypress";

export default defineConfig({
  component: {
    devServer: {
      framework: "svelte",
      bundler: "vite",
    },
  },

  e2e: {
    baseUrl: 'https://studapps.cg.helmo.be:5011/REST_CAO_BART/api',
    setupNodeEvents(on, config) {
      // implement node event listeners here
    },
  },
});

import { defineConfig } from "vite";
import tailwindcss from "@tailwindcss/vite";
import path from "path";
import { fileURLToPath } from "url";

const __dirname = path.dirname(fileURLToPath(import.meta.url));

export default defineConfig({
  base: '/dist/',
  plugins: [
    tailwindcss(),
  ],
  build: {
    watch: {
      include: ['src/main/resources/templates/**/*.html']
    },
    rollupOptions: {
      input: {
        main: path.resolve(__dirname, "./src/main/resources/static/resource/js/main.js"),
        output: path.resolve(__dirname, "./src/main/resources/static/resource/css/main.css"),
      },
      output: {
        entryFileNames: "js/[name].bundle.js",
        dir: path.resolve(__dirname, "./src/main/resources/static/dist"),
        assetFileNames: (assetInfo) => {
          const name = assetInfo.names?.[0] || assetInfo.name;
          if (name && name.endsWith(".css")) {
            return "css/[name][extname]";
          }
          return "assets/[name][extname]";
        },
      },
    },
    minify: "terser",
    terserOptions: {
      compress: {
        drop_console: true,
        drop_debugger: true,
      },
    },
  },
});

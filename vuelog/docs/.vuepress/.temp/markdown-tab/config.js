import {
    CodeTabs
} from "/Users/zhangyekai/IdeaProjects/learn/vuelog/node_modules/.pnpm/@vuepress+plugin-markdown-tab@2.0.0-rc.86_markdown-it@14.1.0_vuepress@2.0.0-rc.20_@vuep_a604c5626cae574594950fe028ea2e5a/node_modules/@vuepress/plugin-markdown-tab/lib/client/components/CodeTabs.js";
import {
    Tabs
} from "/Users/zhangyekai/IdeaProjects/learn/vuelog/node_modules/.pnpm/@vuepress+plugin-markdown-tab@2.0.0-rc.86_markdown-it@14.1.0_vuepress@2.0.0-rc.20_@vuep_a604c5626cae574594950fe028ea2e5a/node_modules/@vuepress/plugin-markdown-tab/lib/client/components/Tabs.js";
import "/Users/zhangyekai/IdeaProjects/learn/vuelog/node_modules/.pnpm/@vuepress+plugin-markdown-tab@2.0.0-rc.86_markdown-it@14.1.0_vuepress@2.0.0-rc.20_@vuep_a604c5626cae574594950fe028ea2e5a/node_modules/@vuepress/plugin-markdown-tab/lib/client/styles/vars.css";

export default {
  enhance: ({ app }) => {
    app.component("CodeTabs", CodeTabs);
    app.component("Tabs", Tabs);
  },
};

import {
    GitContributors
} from "/Users/zhangyekai/IdeaProjects/learn/vuelog/node_modules/.pnpm/@vuepress+plugin-git@2.0.0-rc.88_vuepress@2.0.0-rc.20_@vuepress+bundler-vite@2.0.0-rc.2_75aea4d39fa71dc2eb6834467bfccb04/node_modules/@vuepress/plugin-git/lib/client/components/GitContributors.js";
import {
    GitChangelog
} from "/Users/zhangyekai/IdeaProjects/learn/vuelog/node_modules/.pnpm/@vuepress+plugin-git@2.0.0-rc.88_vuepress@2.0.0-rc.20_@vuepress+bundler-vite@2.0.0-rc.2_75aea4d39fa71dc2eb6834467bfccb04/node_modules/@vuepress/plugin-git/lib/client/components/GitChangelog.js";

export default {
  enhance: ({ app }) => {
    app.component("GitContributors", GitContributors);
    app.component("GitChangelog", GitChangelog);
  },
};

没有加固，直接jadx打开，

包名， com.tencent.mtt

14.2.1.1043:

直接搜“广告”，有不少结果，没啥用，断点不到，

小说阅读页和浏览器页面一样的是BrowserFragment，

断点页面跳转的startActivityForResult发现横幅广告在，
com.qq.e.comm.plugin.base.ad.clickcomponent.chain.VideoFormNode

顺着找到广告管理的类，
com.qq.e.comm.managers.GDTADManager

但就算hook掉initWith方法，还是有广告，

不过现在广告变成了腾讯自家app广告了，
而且点击是在当前BrowserFragment直接展示，

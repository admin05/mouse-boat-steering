# 服务器延迟显示

适用于 Minecraft Java Edition 1.21.11 + Fabric 的纯服务端模组。

## 功能

- 每秒在玩家屏幕下方的操作栏显示该玩家与服务器之间的当前延迟。
- 延迟低于 `100 ms` 显示为绿色。
- 延迟在 `100-199 ms` 显示为黄色。
- 延迟达到 `200 ms` 或以上显示为红色。
- 玩家客户端不需要安装本模组。
- 不依赖 Fabric API。

## 安装

1. 在 Minecraft 1.21.11 服务端安装 Fabric Loader。
2. 将 `server-latency-display-1.0.0.jar` 放入服务端的 `mods` 目录。
3. 启动服务器。

## 构建

需要 JDK 21：

```bash
./gradlew build
```

成品位于 `build/libs/`。

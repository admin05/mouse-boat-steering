# 鼠标划船

适用于 Minecraft Java Edition 26.2 + Fabric 的纯客户端模组。

乘坐并控制船或竹筏时：

- 鼠标左右移动控制船头方向。
- `W`、`S` 仍负责前进和后退。
- `A`、`D` 不再让船转向，避免与鼠标控制冲突。
- 离开船后，鼠标和键盘行为完全恢复原样。

## 安装

1. 安装适用于 Minecraft 26.2 的 Fabric Loader。
2. 将 `mouse-boat-steering-1.0.0.jar` 放入游戏的 `mods` 目录。
3. 本模组不需要 Fabric API，也不需要安装到服务器。

## 构建

需要 JDK 25：

```bash
./gradlew build
```

成品位于 `build/libs/`。

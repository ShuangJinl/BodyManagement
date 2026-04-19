# Body Management（Body Morph）

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.1-green.svg)](https://www.minecraft.net/)
[![Fabric](https://img.shields.io/badge/Loader-Fabric-blue.svg)](https://fabricmc.net/)

面向 **Minecraft 1.21.1 + Fabric** 的玩法向模组：用 **肥胖值** 驱动 **Pehkui** 体型、部分属性与特殊机制（需管理员开启全局玩法）。

> **仓库名** Body Management · **模组 ID** `bodymorph`（`fabric.mod.json` 内名称仍为 *Body Morph*）。

**完整玩法说明（机制、数值、FAQ）：** 请阅读 **[GAMEPLAY.md](GAMEPLAY.md)**。

---

## 功能概览

- **肥胖值**：持久化数据附件，同步到客户端；默认约 50，40–60 视为「正常体型」区间。
- **全局开关**：`/bodymorph start` 开启玩法并重置在线玩家肥胖值；`/bodymorph stop` 关闭并清理缩放与效果；`/bodymorph status` 查看状态（需权限等级 2 / 作弊）。
- **进食**：按食物 `nutrition`（饱食度格）增加肥胖值（倍率见 `FatnessConstants.FOOD_NUTRITION_WEIGHT`）；玩法开启时**饱食度满仍可进食**。
- **移动**：水平移动累积后缓慢降低肥胖值。
- **体型**：Pehkui 宽/高及碰撞相关轴；肥胖值按档位量化后平滑插值（见 `MORPH_FATNESS_STEP`、`MORPH_SCALE_LERP_FACTOR`）。
- **瘦弱**：更高跳跃、骨质疏松摔落提示、挖矿相关效果与掉落倍率等。
- **肥胖**：落地爆炸、疾跑心脏风险、力量/缓慢/饥饿与击杀掉落倍率等。
- **HUD**：左上角彩色显示肥胖值与玩法是否开启（依赖服务端下发的状态包）。
- **非玩家生物**：首次进入服务端世界时，在 **Pehkui 宽、高** 与 **HELD_ITEM** 三轴上各自随机 **0.3–3.0**（玩家与盔甲架除外，且只随机一次并持久标记）。

详细数值与曲线集中在 `FatnessConstants` / `FatnessMath` 中，可按服需求自行微调。

---

## 依赖

| 依赖 | 说明 |
|------|------|
| [Fabric Loader](https://fabricmc.net/use/installer/) | 模组加载器 |
| [Fabric API](https://modrinth.com/mod/fabric-api) | 命令、事件、网络、Data Attachment 等 |
| [Pehkui](https://modrinth.com/mod/pehkui) | `>= 3.8.3`，用于实体缩放 |

---

## 安装（玩家）

1. 安装 **Fabric** 对应 **1.21.1** 版本。  
2. 将 **Fabric API**、**Pehkui** 与本模组 `bodymorph-*.jar` 放入 `.minecraft/mods`。  
3. 单人需 **允许作弊**（或多人 OP）才能使用 `/bodymorph` 指令。

---

## 从源码构建

需要 **JDK 21**。

```bash
# Windows
.\gradlew.bat build

# Linux / macOS
./gradlew build
```

构建产物：`build/libs/bodymorph-<version>.jar`（带 Loom remap，可直接放入 `mods`）。

---

## 仓库中「上传 / 不上传」的约定

| ✅ 应提交 | ❌ 不应提交（已由 `.gitignore` 忽略） |
|-----------|--------------------------------------|
| `src/` 源码与资源 | `build/` 构建输出 |
| `gradle/`、`gradlew*` | `.gradle/` 本地缓存 |
| `build.gradle`、`settings.gradle`、`gradle.properties` | IDE 工程目录 `.idea/`、`.vscode/` 等 |
| `LICENSE`、`README.md`、`.gitignore` | `run/` / `runs/` 本地测试世界 |
| `gradle-wrapper.jar`（Wrapper 二进制） | `.cursor/` 等个人环境配置 |

**不要**提交账号令牌、`.env`、私钥或含隐私路径的本地配置。

---

## 开源协议

本项目以 [MIT License](LICENSE) 发布。

---

## 致谢

- [Fabric](https://fabricmc.net/) 团队与模组生态  
- [Pehkui](https://github.com/Virtuoel/Pehkui)（Virtuoel）  
- 参考常见 Fabric 模组仓库的 README 结构（简介 · 依赖 · 安装 · 构建 · 协议）

---

## English (short)

**Body Management** is a **Fabric 1.21.1** gameplay mod: a synced **fatness** value drives **Pehkui** scaling, attributes, and special rules. Use **`/bodymorph start`** (cheats / permission level 2) to enable; **`/bodymorph stop`** to disable and clean up. **Build:** JDK 21 + `./gradlew build`. **License:** MIT.

Remote repository: [github.com/ShuangJinl/BodyManagement](https://github.com/ShuangJinl/BodyManagement).

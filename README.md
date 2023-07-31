<img src="https://raw.githubusercontent.com/Cozy-Plugins/CozyTreasureHunt/main/graphics/1000x300_banner.png">

<div align=center>
    <a href="https://github.com/Cozy-Plugins/CozyTreasureHunt/releases">
        <img src="./graphics/button_download.png" width="512"></a>
    <a href="">
        <img src="./graphics/button_wiki.png" width="512"></a>
    <a href="https://discord.gg/pax7uFhaaD">
        <img src="./graphics/button_discord.png" width="512"></a>
</div>

[![CodeFactor](https://www.codefactor.io/repository/github/cozy-plugins/cozytreasurehunt/badge)](https://www.codefactor.io/repository/github/cozy-plugins/cozytreasurehunt)
[![Github All Releases](https://img.shields.io/github/downloads/Cozy-Plugins/CozyTreasureHunt/total.svg)](https://github.com/smuddgge/leaf/releases)
[![GitHub issues](https://img.shields.io/github/issues/Cozy-Plugins/cozytreasurehunt.svg)](https://github.com/smuddgge/leaf/issues)
[![GitHub pulls](https://img.shields.io/github/issues-pr/Cozy-Plugins/CozyTreasureHunt.svg)](https://github.com/smuddgge/leaf/pulls)
[![GitHub version](https://img.shields.io/github/v/tag/Cozy-Plugins/CozyTreasureHunt?sort=semver)](https://github.com/smuddgge/leaf/releases)
```yaml
#  _____                                    _   _             _
# |_   _| __ ___  __ _ ___ _   _ _ __ ___  | | | |_   _ _ __ | |_
#   | || '__/ _ \/ _` / __| | | | '__/ _ \ | |_| | | | | '_ \| __|
#   | || | |  __/ (_| \__ \ |_| | | |  __/ |  _  | |_| | | | | |_
#   |_||_|  \___|\__,_|___/\__,_|_|  \___| |_| |_|\__,_|_| |_|\__|
#
# Spigot Plugin
# Organisation : Cozy Plugins
# Author : Smudge
```

[![BStats](https://bstats.org/signatures/bukkit/CozyTreasureHunt.svg)](https://bstats.org/plugin/bukkit/CozyTreasureHunt/19286)

- https://www.spigotmc.org/resources/cozytreasurehunt.111614/
- https://modrinth.com/plugin/cozytreasurehunt

<img src="https://raw.githubusercontent.com/Cozy-Plugins/CozyTreasureHunt/main/graphics/heading_about.png">

\
User-friendly **treasure hunt** creator.

Designed for your **server events**, this plugin will help you **create, log and place** treasure for players to **find, click and gain** a reward!

\
<img src="https://raw.githubusercontent.com/Cozy-Plugins/CozyTreasureHunt/main/graphics/heading_guide.png">

- Create treasure with `/treasure editor`
- Set a treasure location with `/treasure set [name]`
- Spawn the treasure with `/treasure spawn`

<img src="https://i.imgur.com/aswZjRJ.mp4">
<img src="https://i.imgur.com/lxMNRAD.mp4">

# Developers
[![GitHub version](https://img.shields.io/github/v/tag/Cozy-Plugins/CozyTreasureHunt?sort=semver)](https://github.com/smuddgge/leaf/releases)

**Maven**
```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```
```xml
<dependency>
    <groupId>com.github.Cozy-Plugins</groupId>
    <artifactId>CozyTreasureHunt</artifactId>
    <version>Tag</version>
</dependency>
```

**Gradle**
```gradle
allprojects {
    repositories {
    ...
    maven { url 'https://jitpack.io' }
    }
}
```
```gradle
dependencies {
    implementation 'com.github.Cozy-Plugins:CozyTreasureHunt:Tag'
}
```

## Events
```java
@EventHandler()
public void onTreasurePreClick(TreasurePreClickEvent event) {
    // Called when a treasure is clicked.
}

@EventHandler()
public void onTreasurePreClick(TreasurePostClickEvent event) {
    // Called if the pre click event is not canncelled.
}

@EventHandler()
public void onTreasurePreClick(TreasurePreSpawnEvent event) {
    // Called when a treasure is spawned.
}

@EventHandler()
public void onTreasurePreClick(TreasurePostSpawnEvent event) {
    // Called if the pre spawn event is not canncelled.
}
```

## Data
```java
PlayerData playerData = DataStorage.get(player.getUniqueId());
```

<div align=center>
    <a href="https://www.paypal.com/donate/?hosted_button_id=6UNZH6234RBHW"><img src="./graphics/button_donate.png" width="512"></a>
</div>
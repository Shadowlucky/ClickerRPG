package com.example.clickerrpg;

public class Enemy {
    private int id;
    private String name;
    private int lvl;
    private int rewardMn;
    private int rewardXp;
    private int health;
    private int attack;
    private int attackSpd;

    public Enemy(int id, String name, int lvl) {
        this.id = id;
        this.name = name;
        this.lvl = lvl;
    }

    public Enemy(int id, String name, int lvl, int rewardMn, int rewardXp, int health, int attack, int attackSpd) {
        this.id = id;
        this.name = name;
        this.lvl = lvl;
        this.rewardMn = rewardMn;
        this.rewardXp = rewardXp;
        this.health = health;
        this.attack = attack;
        this.attackSpd = attackSpd;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLvl() {
        return lvl;
    }

    public int getRewardMn() {
        return rewardMn;
    }

    public int getRewardXp() {
        return rewardXp;
    }


    public int getHealth() {
        return health;
    }

    public int getAttack() {
        return attack;
    }

    public int getAttackSpd() {
        return attackSpd;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public void setRewardMn(int rewardMn) {
        this.rewardMn = rewardMn;
    }

    public void setRewardXp(int rewardXp) {
        this.rewardXp = rewardXp;
    }


    public void setHealth(int health) {
        this.health = health;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setAttackSpd(int attackSpd) {
        this.attackSpd = attackSpd;
    }
}

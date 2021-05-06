package com.example.clickerrpg;

public class Profile {
    private int id;
    private String name;
    private int money;
    private int xp;
    private int health;
    private int maxHealth;
    private int weaponStage;
    private int potions;

    public Profile(int id, String name, int money) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.xp = 0;
        this.health = 100;
        this.maxHealth = 100;
        this.weaponStage = 1;
        this.potions = 0;
    }

    public Profile(int id, String name, int money, int xp, int health, int maxHealth, int weaponStage, int potions) {
        this.id = id;
        this.name = name;
        this.money = money;
        this.xp = xp;
        this.health = health;
        this.maxHealth = maxHealth;
        this.weaponStage = weaponStage;
        this.potions = potions;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public int getXp() {
        return xp;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getWeaponStage() {
        return weaponStage;
    }

    public int getPotions() {
        return potions;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setXp(int xp) {
        this.xp = xp;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public void setWeaponStage(int weaponStage) {
        this.weaponStage = weaponStage;
    }

    public void setPotions(int potions) {
        this.potions = potions;
    }
}

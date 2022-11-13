/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.unn.piap_serverside;

/**
 *
 * @author STALKER
 */
public class Log {
    public static void debug(String str) {
        System.out.println("Debug: " + str);
    }
    
    public static void error(String str) {
        System.out.println("Error: " + str);
    }
    
    public static void info(String str) {
        System.out.println("Info: " + str);
    }
}

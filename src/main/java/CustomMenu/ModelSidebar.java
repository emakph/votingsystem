/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomMenu;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;

/**
 *
 * @author REYMARK
 */
public class ModelSidebar {

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MenuType getType() {
        return type;
    }

    public void setType(MenuType type) {
        this.type = type;
    }

    public ModelSidebar(String icon, String name, MenuType type) {
        this.icon = icon;
        this.name = name;
        this.type = type;
    }
    
    public ModelSidebar() {
    }
    
    String icon ;
    String name;
    MenuType type;
    
    ClassLoader classLoader = getClass().getClassLoader();
    
    public Icon toIcon(){
        URL imageURL = classLoader.getResource(icon + ".png");
        return new ImageIcon(imageURL);
    }
    
    public static enum MenuType{
        TITLE, MENU, EMPTY
    }
}
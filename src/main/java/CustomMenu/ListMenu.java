/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CustomMenu;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingUtilities;

/**
 *
 * @author REYMARK
 * @param <E>
 */
public class ListMenu<E extends Object> extends JList<E> {
    
    private final DefaultListModel model;
    private int selectedIndex = -1;
    
    public ListMenu(){
        model = new DefaultListModel();
        setModel(model);
        addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent me){
                if(SwingUtilities.isLeftMouseButton(me)) {
                    int index = locationToIndex(me.getPoint());
                    Object o = model.getElementAt(index);
                    if(o instanceof ModelSidebar){
                        ModelSidebar menu = (ModelSidebar)o;
                        if(menu.getType() == ModelSidebar.MenuType.MENU){
                            selectedIndex = index;
                        }
                    } else {
                        selectedIndex = index;
                    }
                    repaint();
                }
            }
        });
    }
    
    @Override
    public ListCellRenderer<? super E> getCellRenderer(){
        return new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> jlist, Object o, int index, boolean selected, boolean focus) {
                ModelSidebar data;
                if (o instanceof ModelSidebar) {
                    data = (ModelSidebar) o;
                } else {
                    data = new ModelSidebar("", o + "", ModelSidebar.MenuType.EMPTY);
                }
                MenuItem item = new MenuItem(data);
                item.setSelected(selectedIndex == index);
                return item;
            }
        };
    }
    
    public void addItem(ModelSidebar data){
        model.addElement(data);
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package customtable;

import java.beans.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

/**
 *
 * @author Daniel Casado
 */
class CustomTable implements ChangeListener, PropertyChangeListener {

    private final JTable tablaPrincipal;
    private final JTable tablaAuxiliar;
    private final JScrollPane scrollPane;

    /*
     *  Especificamos en numero de columnas a dejar fijas y el scrollPane que contiene
     *  la tabla principal.
     */
    CustomTable(int fixedColumns, JScrollPane scrollPane) {
        this.scrollPane = scrollPane;

        tablaPrincipal = ((JTable) scrollPane.getViewport().getView());
        tablaPrincipal.setAutoCreateColumnsFromModel(false);
        tablaPrincipal.addPropertyChangeListener(this);

        //  Usamos la tabla principal para crear el DataModel y
        //  el ListSelectionModel de la auxiliar
        int totalColumns = tablaPrincipal.getColumnCount();

        tablaAuxiliar = new JTable();
        tablaAuxiliar.setAutoCreateColumnsFromModel(false);
        tablaAuxiliar.setModel(tablaPrincipal.getModel());
        tablaAuxiliar.setSelectionModel(tablaPrincipal.getSelectionModel());
        tablaAuxiliar.setFocusable(false);

        //  Eliminamos las columnas a dejar fijas en la tabla principal
        //  y las añadimos a las tabla auxiliar
        for (int i = 0; i < fixedColumns; i++) {
            TableColumnModel columnModel = tablaPrincipal.getColumnModel();
            TableColumn column = columnModel.getColumn(0);
            columnModel.removeColumn(column);
            tablaAuxiliar.getColumnModel().addColumn(column);
        }

        //  Añadimos la tabla auxiliar al scrollPane
        tablaAuxiliar.setPreferredScrollableViewportSize(tablaAuxiliar.getPreferredSize());
        scrollPane.setRowHeaderView(tablaAuxiliar);
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, tablaAuxiliar.getTableHeader());

        // Sincronizamos el scrolling header con la principal
        scrollPane.getRowHeader().addChangeListener(this);
    }

    /*
     *  Devuelve la tabla auxiliar
     */
    public JTable getFixedTable() {
        return tablaAuxiliar;
    }

//  ChangeListener implementado
    public void stateChanged(ChangeEvent e) {
        
        //  Sincronizamos el panel scroll con la cabezera de las fials
        JViewport viewport = (JViewport) e.getSource();
        scrollPane.getVerticalScrollBar().setValue(viewport.getViewPosition().y);
    }

//  PropertyChangeListener implementado
    public void propertyChange(PropertyChangeEvent e) {

        //  Mantiene la tabla auxiliar sincronizada con la principal
        if ("selectionModel".equals(e.getPropertyName())) {
            tablaAuxiliar.setSelectionModel(tablaPrincipal.getSelectionModel());
        }

        if ("model".equals(e.getPropertyName())) {
            tablaAuxiliar.setModel(tablaPrincipal.getModel());
        }
    }
}

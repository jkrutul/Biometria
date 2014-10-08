package zad1.Biometria;

import java.awt.Component;
import javax.swing.*;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class MatrixPanel extends JPanel{
    int _size;
    JTable _table;
    float _matrix[][];
    public MatrixPanel(int size) {
        _size = size;
        _table = new JTable(_size,_size);
        _table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        int vColIndex = 0;
        int width = 25;
        for (vColIndex=0;vColIndex<size;vColIndex++){
            TableColumn col = _table.getColumnModel().getColumn(vColIndex);
            col.setPreferredWidth(width);
            col.setCellEditor(new MyTableCellEditor());
        }
         _matrix = new float[size][size];
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                _table.setValueAt(_matrix[i][j],i, j);
        add(_table);
    }
    
    public float[][] getMatrix() {
        for(int i=0;i<_size;i++)
                    for(int j=0;j<_size;j++)
                        _matrix[i][j]=Float.parseFloat(_table.getValueAt(i, j).toString());
        return _matrix;
    }
    
    public void setSize(int size) {
        _size = size;
        _table.setModel(new JTable(size, size).getModel());
        _matrix = new float[size][size];
        int vColIndex = 0;
        int width = 25;
        for (vColIndex=0;vColIndex<size;vColIndex++){
            TableColumn col = _table.getColumnModel().getColumn(vColIndex);
            col.setPreferredWidth(width);
            col.setCellEditor(new MyTableCellEditor());
        }
        for(int i=0;i<size;i++)
            for(int j=0;j<size;j++)
                _table.setValueAt(_matrix[i][j],i, j);
        _table.revalidate();
    }
        public class MyTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        // This is the component that will handle the editing of the cell value
        JComponent component = new JTextField();

        // This method is called when a cell value is edited by the user.
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int rowIndex, int vColIndex) {
            // 'value' is value contained in the cell located at (rowIndex, vColIndex)

            if (isSelected) {
                // cell (and perhaps other cells) are selected
            }

            // Configure the component with the specified value
            ((JTextField)component).setText(null);

            // Return the configured component
            return component;
        }

        // This method is called when editing is completed.
        // It must return the new value to be stored in the cell.
        @Override
        public Object getCellEditorValue() {
            return ((JTextField)component).getText();
        }
    }
}
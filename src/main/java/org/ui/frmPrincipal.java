package org.ui;

import org.model.Endereco;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.lang.reflect.Field;

public class frmPrincipal extends JFrame {
    private JPanel panelPrincipal;
    private JButton btnEnderecos;
    private JButton bntCadastros;
    private JTable tbEnderecos;
    private JScrollPane panelScroll;
    private JPanel panelTable;
    private JPanel panelOpcoes;
    private JTextField tfBairro;
    private JTextField tfCidade;
    private JTextField tfCEP;
    private JButton btnCadastrar;
    private JPanel panelCadastro;
    private JComboBox cbUF;
    private JButton buscarButton;
    private JTextField tfRua;
    private JTextField tfComplemento;
    private JLabel lbRua;
    private JLabel lbBairro;
    private JLabel lbCidade;
    private JLabel lbComplemento;
    private JLabel lbCEP;
    private JLabel lbUF;

    public frmPrincipal(){
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,900,600);
        this.setContentPane(panelPrincipal);


        DefaultTableModel model = (DefaultTableModel) tbEnderecos.getModel();

        for (Field field : Endereco.class.getFields()) {
            if (!field.getName().toLowerCase().contains("id")) {
                model.addColumn(field.getName());
            }
        }

        for (int i = 0; i < 100; i++) {
            model.addRow(new Object[]{i,i,i,i,i,i,i});
        }

        tbEnderecos.setModel(model);
        tbEnderecos.getTableHeader().setBackground(Color.decode("#44475a"));
        tbEnderecos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tbEnderecos.getTableHeader().setForeground(Color.decode("#f8f8f2"));
        tbEnderecos.getTableHeader().setOpaque(true);
        tbEnderecos.setFont(new Font("Arial", Font.PLAIN, 13));

        LineBorder lineBorder = new LineBorder(Color.decode("#ff79c6"),2, true);

        tfBairro.setBorder(lineBorder);
        tfCEP.setBorder(lineBorder);
        tfCidade.setBorder(lineBorder);
        tfComplemento.setBorder(lineBorder);
        tfRua.setBorder(lineBorder);


        btnEnderecos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelCadastro.setVisible(false);
                panelTable.setVisible(true);
            }
        });
        bntCadastros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelCadastro.setVisible(true);
                panelTable.setVisible(false);
            }
        });
        tbEnderecos.addMouseMotionListener(new MouseMotionAdapter() {
        });
        tbEnderecos.addMouseListener(new MouseAdapter() {
        });
    }

}

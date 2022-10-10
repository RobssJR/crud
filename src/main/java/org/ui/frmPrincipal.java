package org.ui;

import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.controller.CidadesDAO;
import org.controller.DBAccess;
import org.controller.EnderecoDAO;
import org.controller.EstadosDAO;
import org.model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLException;

import static javax.swing.JOptionPane.showMessageDialog;

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
    private JButton btnSalvar;
    private JPanel panelCadastro;
    private JComboBox cbUF;
    private JButton buscarButton;
    private JTextField tfRua;
    private JLabel lbRua;
    private JLabel lbBairro;
    private JLabel lbCidade;
    private JLabel CEPLabel;
    private JLabel lbNumeroCasa;
    private JComboBox cbCidade;
    private JButton btnDeletar;
    private JTextField tfSearch;
    private JTextField tfComplemento;
    private JLabel lbComplemento;
    private JTextField tfNumeroCasa;
    private JLabel lbUF;
    private JButton btnAlterar;
    private JLabel lbTituloCadastro;
    private DefaultTableModel model;
    private IEnderecoDAO enderecoDAO = new EnderecoDAO();
    private ICidadesDAO cidadesDAO = new CidadesDAO();
    private IEstadosDAO estadosDAO = new EstadosDAO();
    private DBAccess dbAccess = DBAccess.getInstance();
    private Endereco enderecoSelecionado;
    private Rectangle tableBound;

    class ResizeListener extends ComponentAdapter {
        public void componentResized(ComponentEvent e) {
            tableBound = new Rectangle(25,50,e.getComponent().getWidth() - 200, e.getComponent().getHeight() - 75);

            panelScroll.setBounds(tableBound);

            tbEnderecos.setSize(tableBound.getSize());
        }
    }

    public frmPrincipal() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,900,600);
        this.setContentPane(panelPrincipal);

        panelPrincipal.remove(panelTable);
        panelPrincipal.remove(panelCadastro);
        panelPrincipal.addComponentListener(new ResizeListener());

        for (Cidades cidade: cidadesDAO.Read()) {
            cbCidade.addItem(cidade);
        }

        for (Estados estado: estadosDAO.Read()) {
            cbUF.addItem(estado);
        }

        carregarModel();

        tbEnderecos.getTableHeader().setBackground(Color.decode("#44475a"));
        tbEnderecos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 15));
        tbEnderecos.getTableHeader().setForeground(Color.decode("#f8f8f2"));
        tbEnderecos.getTableHeader().setOpaque(true);
        tbEnderecos.setFont(new Font("Arial", Font.PLAIN, 13));

        LineBorder lineBorder = new LineBorder(Color.decode("#ff79c6"),2, true);

        tfBairro.setBorder(lineBorder);
        tfCEP.setBorder(lineBorder);
        tfRua.setBorder(lineBorder);
        tfSearch.setBorder(lineBorder);
        tfComplemento.setBorder(lineBorder);
        tfNumeroCasa.setBorder(lineBorder);

        panelTable.setOpaque(false);

        this.setBounds(0,0,1000,680);

        btnEnderecos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                carregarTabela();
            }
        });
        bntCadastros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                carregarCadastro();
            }
        });

        btnSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                boolean validado = (!tfCEP.getText().equals("")
                        && !tfRua.getText().equals("")
                        && !tfBairro.getText().equals("")
                        && !tfComplemento.getText().equals("")
                        && !tfNumeroCasa.getText().equals("")
                        && cbCidade.getSelectedItem() != null);

                try {
                    if (validado) {
                        if (enderecoSelecionado == null) {
                            Endereco endereco = new Endereco();

                            endereco.CEP = tfCEP.getText();
                            endereco.rua = tfRua.getText();
                            endereco.bairro = tfBairro.getText();
                            endereco.complemento = tfComplemento.getText();
                            endereco.cidade = (Cidades) cbCidade.getSelectedItem();
                            endereco.numeroCasa = Integer.parseInt(tfNumeroCasa.getText());

                            enderecoDAO.Insert(endereco);

                        } else {

                            enderecoSelecionado.CEP = tfCEP.getText();
                            enderecoSelecionado.rua = tfRua.getText();
                            enderecoSelecionado.bairro = tfBairro.getText();
                            enderecoSelecionado.complemento = tfComplemento.getText();
                            enderecoSelecionado.cidade = (Cidades) cbCidade.getSelectedItem();
                            enderecoSelecionado.numeroCasa = Integer.parseInt(tfNumeroCasa.getText());

                            enderecoDAO.Insert(enderecoSelecionado);
                        }

                        tfCEP.setText("");
                        tfRua.setText("");
                        tfBairro.setText("");
                        tfComplemento.setText("");
                        tfNumeroCasa.setText("");
                        cbCidade.setSelectedIndex(0);
                        cbUF.setSelectedIndex(0);
                        enderecoSelecionado = null;
                    } else {
                        showMessageDialog(null, "Preencha todos os campos");
                    }

                } catch (Exception exception) {
                    if (exception.getMessage().contains("ConstraintViolationException"))
                        showMessageDialog(null, "Erro ao salvar, verifique se o CEP já foi cadastrado");
                }
            }
        });

        buscarButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                JSONObject jsonObject;
                JSONParser parser = new JSONParser();
                HttpClient client = HttpClient.newHttpClient();

                HttpRequest request = HttpRequest.newBuilder()
                        .GET()
                        .header("accept", "application/json")
                        .uri(URI.create("https://viacep.com.br/ws/" + tfCEP.getText() + "/json/"))
                        .build();

                try {
                    HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                    jsonObject = (JSONObject) parser.parse(response.body());
                    if (jsonObject.get("erro") != "true") {
                        cbCidade.removeAllItems();
                        cbUF.removeAllItems();

                        for (Cidades cidade: cidadesDAO.Read()) {
                            cbCidade.addItem(cidade);
                        }

                        for (Estados estado: estadosDAO.Read()) {
                            cbUF.addItem(estado);
                        }

                        tfComplemento.setText((String) jsonObject.get("complemento"));
                        tfBairro.setText((String) jsonObject.get("bairro"));
                        tfRua.setText((String) jsonObject.get("logradouro"));
                        cbCidade.setSelectedItem(cidadesDAO.SelectByName((String) jsonObject.get("localidade")).get(0));
                        cbUF.setSelectedItem(estadosDAO.SelectByUF((String) jsonObject.get("uf")).get(0));
                    }

                } catch (Exception exception) { showMessageDialog(null, "CEP invalido"); }
            }
        });
        btnDeletar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (tbEnderecos.getSelectedRowCount() > 0) {
                    for (int row : tbEnderecos.getSelectedRows()) {
                        enderecoDAO.Delete((long)model.getValueAt(row,0));
                        model.removeRow(row);
                    }
                } else {
                    showMessageDialog(null, "Selecione uma linha");
                }

            }
        });

        tfSearch.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                String filtro = tfSearch.getText();
                carregarDados(filtro);
            }
        });

        cbUF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cbCidade.removeAllItems();
                try {
                    for (Cidades cidade: cidadesDAO.SelectByUF(cbUF.getSelectedItem().toString())) {
                        cbCidade.addItem(cidade);
                    }
                } catch (Exception exception) { }
            }
        });
        btnAlterar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (tbEnderecos.getSelectedRowCount() > 0) {
                    carregarAlterar();
                } else {
                    showMessageDialog(null, "Selecione uma linha");
                }
            }
        });
    }

    public void carregarDados(String filtro) {
        model = (DefaultTableModel) tbEnderecos.getModel();

        model.setRowCount(0);

        for (Endereco endereco : enderecoDAO.Read()) {
            if(endereco.rua.toLowerCase().contains(filtro.toLowerCase()) || filtro.equals("")) {
                model.addRow(new Object[]{
                        endereco.idEndereco,
                        endereco.rua,
                        String.valueOf(endereco.numeroCasa),
                        endereco.complemento,
                        endereco.bairro,
                        endereco.cidade.nome,
                        endereco.cidade.estados.uf,
                        endereco.CEP,
                        endereco.cidade.idCidade,
                });
            };
        }
    }

    public void carregarModel() {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        model.addColumn("Id");
        model.addColumn("Rua");
        model.addColumn("Numero");
        model.addColumn("Complemento");
        model.addColumn("Bairro");
        model.addColumn("Cidade");
        model.addColumn("UF");
        model.addColumn("CEP");

        carregarDados("");

        tbEnderecos.setModel(model);

        TableColumnModel tableColumnModel = tbEnderecos.getColumnModel();
        tableColumnModel.removeColumn(tableColumnModel.getColumn(0));
    }

    public void carregarTabela() {
        panelPrincipal.add(panelTable, BorderLayout.CENTER);
        panelPrincipal.remove(panelCadastro);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        btnDeletar.setVisible(true);
        btnAlterar.setVisible(true);

        carregarDados("");
    }

    public void carregarCadastro() {
        panelPrincipal.add(panelCadastro, BorderLayout.CENTER);
        panelPrincipal.remove(panelTable);
        panelPrincipal.revalidate();
        panelPrincipal.repaint();

        btnDeletar.setVisible(false);
        btnAlterar.setVisible(false);

        lbTituloCadastro.setText("Cadastro");

        if (enderecoSelecionado != null) {
            tfCEP.setText(enderecoSelecionado.CEP);
            tfRua.setText(enderecoSelecionado.rua);
            tfBairro.setText(enderecoSelecionado.bairro);
            tfComplemento.setText(enderecoSelecionado.complemento);
            tfNumeroCasa.setText(String.valueOf(enderecoSelecionado.numeroCasa));
            cbCidade.setSelectedItem(enderecoSelecionado.cidade);
            cbUF.setSelectedItem(enderecoSelecionado.cidade.estados);

            lbTituloCadastro.setText("Alterar registro");
        }
    }

    public void carregarAlterar() {
        enderecoSelecionado = enderecoDAO.Select((long) model.getValueAt(tbEnderecos.getSelectedRow(),0));
        carregarCadastro();
    }
}

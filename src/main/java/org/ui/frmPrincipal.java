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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
    private JLabel lbRua;
    private JLabel lbBairro;
    private JLabel lbCidade;
    private JLabel CEPLabel;
    private JLabel lbNumeroCasa;
    private JComboBox cbCidade;
    private JButton btnDeletar;
    private JButton bntSalvar;
    private JTextField tfSearch;
    private JTextField tfComplemento;
    private JLabel lbComplemento;
    private JTextField tfNumeroCasa;
    private DefaultTableModel model;

    private IEnderecoDAO enderecoDAO = new EnderecoDAO();
    private ICidadesDAO cidadesDAO = new CidadesDAO();
    private IEstadosDAO estadosDAO = new EstadosDAO();
    private DBAccess dbAccess = DBAccess.getInstance();

    public frmPrincipal() {
        super();

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBounds(0,0,900,600);
        this.setContentPane(panelPrincipal);

        panelPrincipal.remove(panelTable);
        panelPrincipal.remove(panelCadastro);

        for (Cidades cidade: cidadesDAO.Read()) {
            cbCidade.addItem(cidade);
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

        btnEnderecos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelPrincipal.add(panelTable, BorderLayout.CENTER);
                panelPrincipal.remove(panelCadastro);
                panelPrincipal.revalidate();
                panelPrincipal.repaint();

                carregarDados();
            }
        });
        bntCadastros.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                panelPrincipal.add(panelCadastro, BorderLayout.CENTER);
                panelPrincipal.remove(panelTable);
                panelPrincipal.revalidate();
                panelPrincipal.repaint();
            }
        });
        btnCadastrar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                Endereco endereco = new Endereco();

                endereco.CEP = tfCEP.getText();
                endereco.rua = tfRua.getText();
                endereco.bairro = tfBairro.getText();
                endereco.complemento = tfComplemento.getText();
                endereco.cidade = (Cidades) cbCidade.getSelectedItem();
                endereco.numeroCasa = Integer.parseInt(tfNumeroCasa.getText());

                enderecoDAO.Insert(endereco);

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

                    tfComplemento.setText((String) jsonObject.get("complemento"));
                    tfBairro.setText((String) jsonObject.get("bairro"));
                    tfRua.setText((String) jsonObject.get("logradouro"));
                    cbCidade.setSelectedItem(cidadesDAO.Select((String) jsonObject.get("localidade")).get(0));

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        btnDeletar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                for (int row : tbEnderecos.getSelectedRows()) {
                    System.out.println(model.getValueAt(row,0));
                    enderecoDAO.Delete((long)model.getValueAt(row,0));
                    model.removeRow(row);
                }
            }
        });
    }

    public void carregarDados() {
        model = (DefaultTableModel) tbEnderecos.getModel();

        model.setRowCount(0);

        for (Endereco endereco : enderecoDAO.Read()) {
            model.addRow(new Object[]{
                    endereco.idEndereco,
                    endereco.rua,
                    endereco.numeroCasa,
                    endereco.complemento,
                    endereco.bairro,
                    endereco.cidade.nome,
                    endereco.cidade.estados.uf,
                    endereco.CEP

            });
        }
    }

    public void carregarModel() {
        model = (DefaultTableModel) tbEnderecos.getModel();


        model.addColumn("Id");
        model.addColumn("Rua");
        model.addColumn("Numero");
        model.addColumn("Complemento");
        model.addColumn("Bairro");
        model.addColumn("Cidade");
        model.addColumn("UF");
        model.addColumn("CEP");

        carregarDados();

        tbEnderecos.setModel(model);

        TableColumnModel tableColumnModel = tbEnderecos.getColumnModel();
        tableColumnModel.removeColumn(tableColumnModel.getColumn(0));
    }

}

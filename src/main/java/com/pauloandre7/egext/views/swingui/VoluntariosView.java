package com.pauloandre7.egext.views.swingui;

import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.presenters.IVoluntarioPresenter;
import com.pauloandre7.egext.views.IVoluntariosView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class VoluntariosView extends JFrame implements IVoluntariosView {

    private IVoluntarioPresenter presenter;
    private Integer idSelecionado = null;

    private JTextField txtRa;
    private JTextField txtNome;
    private JTextField txtEmail;
    private JTextField txtCelular;

    private JButton btnCadastrar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    private JTable tabelaVoluntarios;
    private DefaultTableModel tableModel;

    public VoluntariosView() {
        super("Projeto Egext - Gestão de Voluntários");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelTop = new JPanel(new BorderLayout(5, 5));
        panelTop.setBorder(new TitledBorder("Cadastro de Voluntário"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtRa = new JTextField(15);
        txtNome = new JTextField(20);
        txtEmail = new JTextField(20);
        txtCelular = new JTextField(15);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("RA:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtRa, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtNome, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("E-mail:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panelCampos.add(txtEmail, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Celular:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        panelCampos.add(txtCelular, gbc);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnLimpar = new JButton("Limpar");
        btnCadastrar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");

        btnLimpar.addActionListener(e -> limparCampos());

        btnCadastrar.addActionListener(e -> {
            if (presenter != null) {
                presenter.registrarVoluntario();
            }
        });

        btnAtualizar.addActionListener(e -> {
            if (presenter != null && idSelecionado != null) {
                presenter.modificarVoluntario(idSelecionado);
            } else if (idSelecionado == null) {
                exibirMensagemErro("Selecione um voluntário na tabela para atualizar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            if (presenter != null && idSelecionado != null) {
                presenter.excluirVoluntario(idSelecionado);
                limparCampos();
            } else if (idSelecionado == null) {
                exibirMensagemErro("Selecione um voluntário na tabela para excluir.");
            }
        });

        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnCadastrar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);

        panelTop.add(panelCampos, BorderLayout.CENTER);
        panelTop.add(panelBotoes, BorderLayout.SOUTH);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(new TitledBorder("Voluntários Cadastrados"));

        String[] colunas = {"ID", "RA", "Nome", "E-mail", "Celular"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaVoluntarios = new JTable(tableModel);
        tabelaVoluntarios.setFillsViewportHeight(true);
        tabelaVoluntarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaVoluntarios.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaVoluntarios.getSelectedRow();
                if (row >= 0) {
                    idSelecionado = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtRa.setText(tableModel.getValueAt(row, 1).toString());
                    txtNome.setText(tableModel.getValueAt(row, 2).toString());
                    txtEmail.setText(tableModel.getValueAt(row, 3).toString());
                    txtCelular.setText(tableModel.getValueAt(row, 4).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaVoluntarios);
        panelBottom.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTop, BorderLayout.NORTH);
        mainPanel.add(panelBottom, BorderLayout.CENTER);

        add(mainPanel);
    }

    public void setPresenter(IVoluntarioPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getNomeInput() {
        return txtNome.getText().trim();
    }

    @Override
    public String getEmailInput() {
        return txtEmail.getText().trim();
    }

    @Override
    public String getCelularInput() {
        return txtCelular.getText().trim();
    }

    @Override
    public String getRaInput() {
        return txtRa.getText().trim();
    }

    @Override
    public void listarVoluntarios(List<VoluntarioRecord> voluntarios) {
        tableModel.setRowCount(0);
        if (voluntarios != null) {
            for (VoluntarioRecord v : voluntarios) {
                if (v != null) {
                    Object[] row = {
                        v.id(),
                        v.ra(),
                        v.nome(),
                        v.email(),
                        v.celular()
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    @Override
    public void exibirMensagem(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Sucesso", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void exibirMensagemErro(String erro) {
        JOptionPane.showMessageDialog(this, erro, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void limparCampos() {
        idSelecionado = null;
        txtRa.setText("");
        txtNome.setText("");
        txtEmail.setText("");
        txtCelular.setText("");
        tabelaVoluntarios.clearSelection();
        txtRa.requestFocus();
    }
}

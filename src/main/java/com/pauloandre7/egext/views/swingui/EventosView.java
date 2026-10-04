package com.pauloandre7.egext.views.swingui;

import com.pauloandre7.egext.models.EventoRecord;
import com.pauloandre7.egext.models.InscritoRecord;
import com.pauloandre7.egext.models.StatusEvento;
import com.pauloandre7.egext.models.VoluntarioRecord;
import com.pauloandre7.egext.presenters.EventoPresenter;
import com.pauloandre7.egext.presenters.IEventoPresenter;
import com.pauloandre7.egext.views.IEventosView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class EventosView extends JFrame implements IEventosView {

    private IEventoPresenter presenter;
    private int idSelecionado = -1;

    private JTextField txtTitulo;
    private JTextField txtData;
    private JTextField txtLocal;
    private JTextField txtResponsavel;
    private JTextField txtVoluntarios;
    private JTextField txtInscritos;
    private JComboBox<String> cbStatus;

    private JButton btnCriar;
    private JButton btnAtualizar;
    private JButton btnExcluir;
    private JButton btnLimpar;

    private JTable tabelaEventos;
    private DefaultTableModel tableModel;

    public EventosView() {
        super("Egext - Gestão de Eventos");
        initUI();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // =========================================================================
        // BARRA DE MENU SUPERIOR PARA NAVEGAÇÃO ENTRE MÓDULOS
        // =========================================================================
        JMenuBar menuBar = new JMenuBar();
        JMenu menuNavegacao = new JMenu("Navegação");

        JMenuItem itemVoluntarios = new JMenuItem("Gerenciar Voluntários");
        itemVoluntarios.addActionListener(e -> {
            if (presenter != null) {
                presenter.abrirTelaVoluntarios();
            } else {
                exibirMensagemErro("Erro: Presenter não foi associado à tela de Eventos.");
            }
        });

        JMenuItem itemInscritos = new JMenuItem("Gerenciar Inscritos");
        itemInscritos.addActionListener(e -> {
            if (presenter != null) {
                presenter.abrirTelaInscritos();
            } else {
                exibirMensagemErro("Erro: Presenter não foi associado à tela de Eventos.");
            }
        });

        menuNavegacao.add(itemVoluntarios);
        menuNavegacao.add(itemInscritos);
        menuBar.add(menuNavegacao);
        setJMenuBar(menuBar);

        // =========================================================================
        // PAINEL PRINCIPAL
        // =========================================================================
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelTop = new JPanel(new BorderLayout(5, 5));
        panelTop.setBorder(new TitledBorder("Cadastro e Edição de Eventos"));

        JPanel panelCampos = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtTitulo = new JTextField(20);
        txtData = new JTextField(15);
        txtLocal = new JTextField(20);
        txtResponsavel = new JTextField(20);
        txtVoluntarios = new JTextField(20);
        txtInscritos = new JTextField(20);
        cbStatus = new JComboBox<>(new String[]{"PLANEJAMENTO", "EM_ANDAMENTO", "CONCLUIDO", "CANCELADO"});

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtTitulo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Data:"), gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1.0;
        panelCampos.add(txtData, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Local:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1.0;
        panelCampos.add(txtLocal, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Responsável:"), gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1.0;
        panelCampos.add(txtResponsavel, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Voluntários (vírgula):"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 1.0;
        panelCampos.add(txtVoluntarios, gbc);

        gbc.gridx = 2; gbc.gridy = 2; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Inscritos (vírgula):"), gbc);
        gbc.gridx = 3; gbc.gridy = 2; gbc.weightx = 1.0;
        panelCampos.add(txtInscritos, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.0;
        panelCampos.add(new JLabel("Status:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 1.0;
        panelCampos.add(cbStatus, gbc);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnLimpar = new JButton("Limpar");
        btnCriar = new JButton("Cadastrar");
        btnAtualizar = new JButton("Atualizar");
        btnExcluir = new JButton("Excluir");

        btnLimpar.addActionListener(e -> limparCampos());

        btnCriar.addActionListener(e -> {
            if (presenter != null) {
                presenter.criarEvento();
            }
        });

        btnAtualizar.addActionListener(e -> {
            if (presenter != null && idSelecionado != -1) {
                presenter.modificarEvento(idSelecionado);
            } else if (idSelecionado == -1) {
                exibirMensagemErro("Selecione um evento na tabela para atualizar.");
            }
        });

        btnExcluir.addActionListener(e -> {
            if (presenter != null && idSelecionado != -1) {
                presenter.excluirEvento(idSelecionado);
                limparCampos();
            } else if (idSelecionado == -1) {
                exibirMensagemErro("Selecione um evento na tabela para excluir.");
            }
        });

        panelBotoes.add(btnLimpar);
        panelBotoes.add(btnCriar);
        panelBotoes.add(btnAtualizar);
        panelBotoes.add(btnExcluir);

        panelTop.add(panelCampos, BorderLayout.CENTER);
        panelTop.add(panelBotoes, BorderLayout.SOUTH);

        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBorder(new TitledBorder("Eventos Cadastrados"));

        String[] colunas = {"ID", "Título", "Data", "Local", "Responsável", "Voluntários", "Inscritos", "Status"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaEventos = new JTable(tableModel);
        tabelaEventos.setFillsViewportHeight(true);
        tabelaEventos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaEventos.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tabelaEventos.getSelectedRow();
                if (row >= 0) {
                    idSelecionado = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
                    txtTitulo.setText(tableModel.getValueAt(row, 1).toString());
                    txtData.setText(tableModel.getValueAt(row, 2).toString());
                    txtLocal.setText(tableModel.getValueAt(row, 3).toString());
                    txtResponsavel.setText(tableModel.getValueAt(row, 4).toString());
                    txtVoluntarios.setText(tableModel.getValueAt(row, 5).toString());
                    txtInscritos.setText(tableModel.getValueAt(row, 6).toString());
                    cbStatus.setSelectedItem(tableModel.getValueAt(row, 7).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabelaEventos);
        panelBottom.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(panelTop, BorderLayout.NORTH);
        mainPanel.add(panelBottom, BorderLayout.CENTER);

        add(mainPanel);
    }

    public void setPresenter(EventoPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public String getTituloInput() {
        return txtTitulo.getText().trim();
    }

    @Override
    public String getDataInput() {
        return txtData.getText().trim();
    }

    @Override
    public String getLocalInput() {
        return txtLocal.getText().trim();
    }

    @Override
    public String getResponsavelInput() {
        return txtResponsavel.getText().trim();
    }

    @Override
    public String[] getVoluntariosInput() {
        String texto = txtVoluntarios.getText().trim();
        if (texto.isEmpty()) {
            return new String[0];
        }
        return texto.split(",");
    }

    @Override
    public String[] getInscritosInput() {
        String texto = txtInscritos.getText().trim();
        if (texto.isEmpty()) {
            return new String[0];
        }
        return texto.split(",");
    }

    @Override
    public String getStatusInput() {
        Object selected = cbStatus.getSelectedItem(); 
        if (selected == null) { 
            return "PLANEJAMENTO"; 
        }
        
        String statusStr = selected.toString(); 
        if (statusStr.contains(".")) { 
            statusStr = statusStr.substring(statusStr.lastIndexOf(".") + 1); 
        } 
        return statusStr;
    }

    @Override
    public void listarEventos(List<EventoRecord> eventos) {
        tableModel.setRowCount(0);
        if (eventos != null) {
            for (EventoRecord e : eventos) {
                if (e != null) {
                    String resp = e.responsavel() != null ? e.responsavel().nome() : "";
                    String vols = e.voluntariosInternos() != null ? 
                        e.voluntariosInternos().stream().map(VoluntarioRecord::nome).collect(Collectors.joining(", ")) : "";
                    String inss = e.inscritos() != null ? 
                        e.inscritos().stream().map(InscritoRecord::nome).collect(Collectors.joining(", ")) : "";
                    String st = e.statusEvento() != null ? e.statusEvento().toString() : "";

                    Object[] row = {
                        e.idEv(),
                        e.titulo(),
                        e.data(),
                        e.local(),
                        resp,
                        vols,
                        inss,
                        st
                    };
                    tableModel.addRow(row);
                }
            }
        }
    }

    @Override
    public void detalharEvento(EventoRecord evento) {
        if (evento != null) {
            String resp = evento.responsavel() != null ? evento.responsavel().nome() : "Nenhum";
            String vols = evento.voluntariosInternos() != null ? 
                evento.voluntariosInternos().stream().map(VoluntarioRecord::nome).collect(Collectors.joining(", ")) : "Nenhum";
            String inss = evento.inscritos() != null ? 
                evento.inscritos().stream().map(InscritoRecord::nome).collect(Collectors.joining(", ")) : "Nenhum";

            String detalhe = String.format("ID: %d\nTítulo: %s\nData: %s\nLocal: %s\nResponsável: %s\nVoluntários: %s\nInscritos: %s\nStatus: %s",
                evento.idEv(), evento.titulo(), evento.data(), evento.local(), resp, vols, inss, evento.statusEvento());
            JOptionPane.showMessageDialog(this, detalhe, "Detalhes do Evento", JOptionPane.INFORMATION_MESSAGE);
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

    public void limparCampos() {
        idSelecionado = -1;
        txtTitulo.setText("");
        txtData.setText("");
        txtLocal.setText("");
        txtResponsavel.setText("");
        txtVoluntarios.setText("");
        txtInscritos.setText("");
        cbStatus.setSelectedIndex(0);
        tabelaEventos.clearSelection();
        txtTitulo.requestFocus();
    }
}

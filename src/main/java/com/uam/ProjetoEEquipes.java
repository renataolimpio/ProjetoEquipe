package com.uam;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import com.uam.repositories.*;
import com.uam.models.*;
import com.uam.services.*;


public class ProjetoEEquipes {
    // Repositórios
    private final UserRepository userRepo = new UserRepository();
    private final ProjectRepository projectRepo = new ProjectRepository();
    private final TeamRepository teamRepo = new TeamRepository();

    // Serviços
    private final UserService userService = new UserService(userRepo);
    private final ProjectService projectService = new ProjectService(projectRepo, userRepo);
    private final TeamService teamService = new TeamService(teamRepo, userRepo, projectRepo);

    // UI
    private final JFrame frame = new JFrame("Projeto e Equipes");

    // Models compartilhados (para atualização fácil)
    private final DefaultListModel<User> usersListModel = new DefaultListModel<>();
    private final DefaultListModel<Project> projectsListModel = new DefaultListModel<>();
    private final DefaultListModel<Team> teamsListModel = new DefaultListModel<>();
    private final DefaultComboBoxModel<User> gerenteComboModel = new DefaultComboBoxModel<>();

    // Models para as listas locais da aba de equipes
    private final DefaultListModel<User> availableUsersModel = new DefaultListModel<>();
    private final DefaultListModel<Project> availableProjectsModel = new DefaultListModel<>();

    // Formatador de datas
    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // ========== CONSTANTES DE DESIGN MODERNO ==========

    // Cores principais
    private static final Color PRIMARY_COLOR = new Color(52, 152, 219);       // Azul moderno
    private static final Color SECONDARY_COLOR = new Color(46, 204, 113);     // Verde
    private static final Color ACCENT_COLOR = new Color(230, 126, 34);        // Laranja
    private static final Color SUCCESS_COLOR = new Color(39, 174, 96);        // Verde sucesso

    // Cores de fundo
    private static final Color BG_PRIMARY = new Color(248, 249, 250);         // Cinza muito claro
    private static final Color BG_SECONDARY = new Color(255, 255, 255);       // Branco
    private static final Color BG_CARD = new Color(255, 255, 255);            // Branco para cards

    // Cores de texto
    private static final Color TEXT_PRIMARY = new Color(52, 58, 64);          // Cinza escuro
    private static final Color TEXT_SECONDARY = new Color(108, 117, 125);     // Cinza médio
    private static final Color TEXT_LIGHT = new Color(255, 255, 255);         // Branco

    // Cores de borda
    private static final Color BORDER_LIGHT = new Color(222, 226, 230);       // Borda clara

    // Fontes
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.PLAIN, 12);
    private static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 11);

    public ProjetoEEquipes() {
        // Configurar Look and Feel moderno
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
            // Customizar cores do UI Manager
            UIManager.put("TabbedPane.selected", BG_CARD);
            UIManager.put("TabbedPane.background", BG_PRIMARY);
            UIManager.put("Button.background", PRIMARY_COLOR);
            UIManager.put("Button.foreground", TEXT_LIGHT);
        } catch (Exception e) {
            // Fallback para o look padrão
            System.out.println("Não foi possível aplicar o Look and Feel moderno. Usando padrão.");
        }

        initDummyData();
        initUI();
    }

    private JButton createStyledButton(String text, Color bgColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(FONT_BUTTON);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Adicionar efeito hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        // Padding interno
        button.setMargin(new Insets(8, 16, 8, 16));

        return button;
    }

    /**
     * Cria um painel com borda arredondada e sombra
     */
    private JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BG_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    /**
     * Cria um label estilizado
     */
    private JLabel createStyledLabel(String text, Font font, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(color);
        return label;
    }

    /**
     * Cria um campo de texto estilizado
     */
    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(FONT_LABEL);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setBackground(BG_SECONDARY);

        // Placeholder effect
        if (placeholder != null && !placeholder.isEmpty()) {
            field.setText(placeholder);
            field.setForeground(TEXT_SECONDARY);
            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent evt) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(TEXT_PRIMARY);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent evt) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(TEXT_SECONDARY);
                    }
                }
            });
        }

        return field;
    }

    /**
     * Cria um título de seção
     */
    private JLabel createSectionTitle(String text) {
        JLabel title = new JLabel(text);
        title.setFont(FONT_TITLE);
        title.setForeground(TEXT_PRIMARY);
        title.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        return title;
    }

    /**
     * Adiciona um campo ao formulário com layout GridBag
     */
    private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, JComponent field, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 0;

        JLabel label = createStyledLabel(labelText, FONT_LABEL, TEXT_PRIMARY);
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        panel.add(field, gbc);
    }

    private void initDummyData() {
        // cria alguns usuários iniciais
        User admin = userService.criarUsuario("Admin System", "00000000000", "admin@ex.com", "Administrador", "admin", "admin", Role.ADMINISTRADOR);
        User gerente = userService.criarUsuario("Maria Gerente", "11111111111", "maria@ex.com", "Gerente de Projetos", "maria", "senha", Role.GERENTE);
        User colab1 = userService.criarUsuario("João Colaborador", "22222222222", "joao@ex.com", "Desenvolvedor", "joao", "1234", Role.COLABORADOR);

        Project p = projectService.criarProjeto("Projeto Alpha", "Projeto piloto", LocalDate.now(), LocalDate.now().plusMonths(2), ProjectStatus.EM_ANDAMENTO, gerente);

        Team t = teamService.criarEquipe("Equipe A", "Equipe de desenvolvimento A");
        teamService.adicionarMembro(t, gerente);
        teamService.adicionarMembro(t, colab1);
        teamService.adicionarMembro(t, admin);
        teamService.associarProjeto(t, p);

        refreshAllModels();
    }

    private void initUI() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLayout(new BorderLayout());

        // Configurar fundo da janela principal
        frame.getContentPane().setBackground(BG_PRIMARY);

        // Criar título da aplicação
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        JLabel titleLabel = new JLabel("Sistema de Gestão de Projetos e Equipes");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(TEXT_LIGHT);
        titleLabel.setIcon(null);

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setBackground(PRIMARY_COLOR);
        titleContainer.add(titleLabel, BorderLayout.CENTER);

        headerPanel.add(titleContainer, BorderLayout.WEST);

        // Criar tabs estilizadas
        JTabbedPane tabs = new JTabbedPane();
        tabs.setFont(FONT_LABEL);
        tabs.setBackground(BG_PRIMARY);
        tabs.setTabPlacement(JTabbedPane.TOP);

        // Adicionar tabs com ícones (simulados com emojis)
        tabs.addTab("Usuários", buildUserPanel());
        tabs.addTab("Projetos", buildProjectPanel());
        tabs.addTab("Equipes", buildTeamPanel());
        tabs.addTab("Relatórios", buildOverviewPanel());

        // Container principal
        JPanel mainContainer = new JPanel(new BorderLayout());
        mainContainer.setBackground(BG_PRIMARY);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainContainer.add(tabs, BorderLayout.CENTER);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(mainContainer, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel buildUserPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do formulário
        JPanel formCard = createCardPanel();
        formCard.setLayout(new BorderLayout());

        // Título do formulário
        JLabel formTitle = createSectionTitle("Cadastro de Usuários");
        formCard.add(formTitle, BorderLayout.NORTH);

        // Formulário principal
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos do formulário
        JTextField nomeField = createStyledTextField("Digite o nome completo");
        JTextField cpfField = createStyledTextField("000.000.000-00");
        JTextField emailField = createStyledTextField("usuario@exemplo.com");
        JTextField cargoField = createStyledTextField("Ex: Desenvolvedor");
        JTextField loginField = createStyledTextField("username");
        JPasswordField senhaField = new JPasswordField();
        senhaField.setFont(FONT_LABEL);
        senhaField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_LIGHT, 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));

        JComboBox<Role> perfilBox = new JComboBox<>(Role.values());
        perfilBox.setFont(FONT_LABEL);
        perfilBox.setBackground(BG_SECONDARY);

        // Layout dos campos
        int row = 0;
        addFormField(formPanel, gbc, "Nome Completo:", nomeField, row++);
        addFormField(formPanel, gbc, "CPF:", cpfField, row++);
        addFormField(formPanel, gbc, "E-mail:", emailField, row++);
        addFormField(formPanel, gbc, "Cargo:", cargoField, row++);
        addFormField(formPanel, gbc, "Login:", loginField, row++);
        addFormField(formPanel, gbc, "Senha:", senhaField, row++);
        addFormField(formPanel, gbc, "Perfil:", perfilBox, row++);

        formCard.add(formPanel, BorderLayout.CENTER);

        // Botão de salvar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_CARD);
        JButton btnSalvar = createStyledButton("Salvar Usuário", SUCCESS_COLOR, TEXT_LIGHT);
        buttonPanel.add(btnSalvar);
        formCard.add(buttonPanel, BorderLayout.SOUTH);

        // Painel da lista
        JPanel listCard = createCardPanel();
        listCard.setLayout(new BorderLayout());

        JLabel listTitle = createSectionTitle("Usuários Cadastrados");
        listCard.add(listTitle, BorderLayout.NORTH);

        // Lista estilizada
        JList<User> userList = new JList<>(usersListModel);
        userList.setFont(FONT_LABEL);
        userList.setBackground(BG_SECONDARY);
        userList.setSelectionBackground(PRIMARY_COLOR);
        userList.setSelectionForeground(TEXT_LIGHT);
        userList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(userList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        scrollPane.getViewport().setBackground(BG_SECONDARY);
        listCard.add(scrollPane, BorderLayout.CENTER);

        // Event listener do botão
        btnSalvar.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String cpf = cpfField.getText().trim();
                String email = emailField.getText().trim();
                String cargo = cargoField.getText().trim();
                String login = loginField.getText().trim();
                String senha = new String(senhaField.getPassword());
                Role perfil = (Role) perfilBox.getSelectedItem();

                userService.criarUsuario(nome, cpf, email, cargo, login, senha, perfil);
                JOptionPane.showMessageDialog(frame, "Usuário criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearUserForm(nomeField, cpfField, emailField, cargoField, loginField, senhaField);
                refreshAllModels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, listCard);
        splitPane.setDividerLocation(500);
        splitPane.setBackground(BG_PRIMARY);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void clearUserForm(JTextField nome, JTextField cpf, JTextField email, JTextField cargo, JTextField login, JPasswordField senha) {
        nome.setText(""); cpf.setText(""); email.setText(""); cargo.setText(""); login.setText(""); senha.setText("");
    }

    private JPanel buildProjectPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel do formulário
        JPanel formCard = createCardPanel();
        formCard.setLayout(new BorderLayout());

        // Título do formulário
        JLabel formTitle = createSectionTitle("Cadastro de Projetos");
        formCard.add(formTitle, BorderLayout.NORTH);

        // Formulário principal
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos do formulário
        JTextField nomeField = createStyledTextField("Nome do projeto");
        JTextField descField = createStyledTextField("Descrição do projeto");
        JTextField dataInicioField = createStyledTextField("yyyy-MM-dd");
        JTextField dataTerminoField = createStyledTextField("yyyy-MM-dd");

        JComboBox<ProjectStatus> statusBox = new JComboBox<>(ProjectStatus.values());
        statusBox.setFont(FONT_LABEL);
        statusBox.setBackground(BG_SECONDARY);

        JComboBox<User> gerenteBox = new JComboBox<>(gerenteComboModel);
        gerenteBox.setFont(FONT_LABEL);
        gerenteBox.setBackground(BG_SECONDARY);

        // Layout dos campos
        int row = 0;
        addFormField(formPanel, gbc, "Nome do Projeto:", nomeField, row++);
        addFormField(formPanel, gbc, "Descrição:", descField, row++);
        addFormField(formPanel, gbc, "Data Início:", dataInicioField, row++);
        addFormField(formPanel, gbc, "Data Término:", dataTerminoField, row++);
        addFormField(formPanel, gbc, "Status:", statusBox, row++);
        addFormField(formPanel, gbc, "Gerente:", gerenteBox, row++);

        formCard.add(formPanel, BorderLayout.CENTER);

        // Botão de salvar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_CARD);
        JButton btnSalvar = createStyledButton("Salvar Projeto", PRIMARY_COLOR, TEXT_LIGHT);
        buttonPanel.add(btnSalvar);
        formCard.add(buttonPanel, BorderLayout.SOUTH);

        // Painel da lista
        JPanel listCard = createCardPanel();
        listCard.setLayout(new BorderLayout());

        JLabel listTitle = createSectionTitle("Projetos Cadastrados");
        listCard.add(listTitle, BorderLayout.NORTH);

        // Lista estilizada
        JList<Project> projList = new JList<>(projectsListModel);
        projList.setFont(FONT_LABEL);
        projList.setBackground(BG_SECONDARY);
        projList.setSelectionBackground(PRIMARY_COLOR);
        projList.setSelectionForeground(TEXT_LIGHT);
        projList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(projList);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        scrollPane.getViewport().setBackground(BG_SECONDARY);
        listCard.add(scrollPane, BorderLayout.CENTER);

        // Event listener do botão
        btnSalvar.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String desc = descField.getText().trim();
                LocalDate di = parseDateOrNull(dataInicioField.getText().trim());
                LocalDate dt = parseDateOrNull(dataTerminoField.getText().trim());
                ProjectStatus status = (ProjectStatus) statusBox.getSelectedItem();
                User gerente = (User) gerenteBox.getSelectedItem();

                projectService.criarProjeto(nome, desc, di, dt, status, gerente);
                JOptionPane.showMessageDialog(frame, "Projeto criado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearProjectForm(nomeField, descField, dataInicioField, dataTerminoField);
                refreshAllModels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, formCard, listCard);
        splitPane.setDividerLocation(500);
        splitPane.setBackground(BG_PRIMARY);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    private void clearProjectForm(JTextField nome, JTextField desc, JTextField di, JTextField dt) { nome.setText(""); desc.setText(""); di.setText("yyyy-MM-dd"); dt.setText("yyyy-MM-dd"); }

    private LocalDate parseDateOrNull(String s) {
        try { if (s == null || s.isBlank()) return null; return LocalDate.parse(s, dtf); }
        catch (Exception ex) { return null; }
    }

    private JPanel buildTeamPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel único do formulário + seleção
        JPanel formCard = createCardPanel();
        formCard.setLayout(new BorderLayout());

        // Título do formulário
        JLabel formTitle = createSectionTitle("Criação de Equipes");
        formCard.add(formTitle, BorderLayout.NORTH);

        // Container principal do formulário
        JPanel mainFormPanel = new JPanel(new GridBagLayout());
        mainFormPanel.setBackground(BG_CARD);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Campos básicos
        JTextField nomeField = createStyledTextField("Nome da equipe");
        JTextField descField = createStyledTextField("Descrição da equipe");

        // Layout dos campos básicos
        int row = 0;
        addFormField(mainFormPanel, gbc, "Nome da Equipe:", nomeField, row++);
        addFormField(mainFormPanel, gbc, "Descrição:", descField, row++);

        // Adicionar espaçamento entre os campos e as listas de seleção
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(20, 8, 10, 8);
        JLabel selectionLabel = createStyledLabel("Selecionar Membros e Projetos:", FONT_TITLE, TEXT_PRIMARY);
        mainFormPanel.add(selectionLabel, gbc);

        // Listas de seleção
        JList<User> availableUsersList = new JList<>(availableUsersModel);
        availableUsersList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableUsersList.setFont(FONT_LABEL);
        availableUsersList.setBackground(BG_SECONDARY);
        availableUsersList.setSelectionBackground(SECONDARY_COLOR);
        availableUsersList.setSelectionForeground(TEXT_LIGHT);

        JList<Project> availableProjectsList = new JList<>(availableProjectsModel);
        availableProjectsList.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        availableProjectsList.setFont(FONT_LABEL);
        availableProjectsList.setBackground(BG_SECONDARY);
        availableProjectsList.setSelectionBackground(ACCENT_COLOR);
        availableProjectsList.setSelectionForeground(TEXT_LIGHT);

        // Painel das listas de seleção
        JPanel selectionPanel = new JPanel(new GridLayout(1,2,10,10));
        selectionPanel.setBackground(BG_CARD);

        JPanel userPanel = new JPanel(new BorderLayout());
        userPanel.setBackground(BG_CARD);
        userPanel.add(createStyledLabel("Usuários Disponíveis:", FONT_LABEL, TEXT_PRIMARY), BorderLayout.NORTH);
        JScrollPane userScroll = new JScrollPane(availableUsersList);
        userScroll.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        userScroll.setPreferredSize(new Dimension(200, 150));
        userPanel.add(userScroll, BorderLayout.CENTER);

        JPanel projectPanel = new JPanel(new BorderLayout());
        projectPanel.setBackground(BG_CARD);
        projectPanel.add(createStyledLabel("Projetos Disponíveis:", FONT_LABEL, TEXT_PRIMARY), BorderLayout.NORTH);
        JScrollPane projectScroll = new JScrollPane(availableProjectsList);
        projectScroll.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        projectScroll.setPreferredSize(new Dimension(200, 150));
        projectPanel.add(projectScroll, BorderLayout.CENTER);

        selectionPanel.add(userPanel);
        selectionPanel.add(projectPanel);

        // Adicionar o painel de seleção ao formulário principal
        gbc.gridx = 0;
        gbc.gridy = row++;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(8, 8, 8, 8);
        mainFormPanel.add(selectionPanel, gbc);

        formCard.add(mainFormPanel, BorderLayout.CENTER);

        // Botão de salvar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_CARD);
        JButton btnSalvar = createStyledButton("Criar Equipe", ACCENT_COLOR, TEXT_LIGHT);
        buttonPanel.add(btnSalvar);
        formCard.add(buttonPanel, BorderLayout.SOUTH);

        // Painel da lista de equipes
        JPanel listCard = createCardPanel();
        listCard.setLayout(new BorderLayout());

        JLabel listTitle = createSectionTitle("Equipes Criadas");
        listCard.add(listTitle, BorderLayout.NORTH);

        // Lista estilizada
        JList<Team> teamList = new JList<>(teamsListModel);
        teamList.setFont(FONT_LABEL);
        teamList.setBackground(BG_SECONDARY);
        teamList.setSelectionBackground(PRIMARY_COLOR);
        teamList.setSelectionForeground(TEXT_LIGHT);
        teamList.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane teamScroll = new JScrollPane(teamList);
        teamScroll.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        listCard.add(teamScroll, BorderLayout.CENTER);

        // Event listener do botão
        btnSalvar.addActionListener(e -> {
            try {
                String nome = nomeField.getText().trim();
                String desc = descField.getText().trim();
                Team t = teamService.criarEquipe(nome, desc);
                for (User u : availableUsersList.getSelectedValuesList()) teamService.adicionarMembro(t, u);
                for (Project p : availableProjectsList.getSelectedValuesList()) teamService.associarProjeto(t, p);
                JOptionPane.showMessageDialog(frame, "Equipe criada com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                clearTeamForm(nomeField, descField);
                // Limpar seleções
                availableUsersList.clearSelection();
                availableProjectsList.clearSelection();
                refreshAllModels();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Layout principal - dividir em 2 seções (formulário+seleção e lista)
        JSplitPane mainSplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT, formCard, listCard);
        mainSplit.setDividerLocation(450);
        mainSplit.setBackground(BG_PRIMARY);
        mainSplit.setBorder(null);

        mainPanel.add(mainSplit, BorderLayout.CENTER);
        return mainPanel;
    }

    private void clearTeamForm(JTextField nome, JTextField desc) { nome.setText(""); desc.setText(""); }

    private JPanel buildOverviewPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_PRIMARY);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Painel de estatísticas (cards no topo)
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        statsPanel.setBackground(BG_PRIMARY);

        // Card de Usuários
        JPanel userCard = createStatsCard("U", "Usuários", String.valueOf(userService.listarUsuarios().size()), PRIMARY_COLOR);

        // Card de Projetos
        JPanel projectCard = createStatsCard("P", "Projetos", String.valueOf(projectService.listarProjetos().size()), SECONDARY_COLOR);

        // Card de Equipes
        JPanel teamCard = createStatsCard("E", "Equipes", String.valueOf(teamService.listarEquipes().size()), ACCENT_COLOR);

        statsPanel.add(userCard);
        statsPanel.add(projectCard);
        statsPanel.add(teamCard);

        // Painel de relatório detalhado
        JPanel reportCard = createCardPanel();
        reportCard.setLayout(new BorderLayout());

        JLabel reportTitle = createSectionTitle("Relatório Detalhado do Sistema");
        reportCard.add(reportTitle, BorderLayout.NORTH);

        JTextArea area = new JTextArea();
        area.setEditable(false);
        area.setFont(new Font("Consolas", Font.PLAIN, 12));
        area.setBackground(BG_SECONDARY);
        area.setForeground(TEXT_PRIMARY);
        area.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_LIGHT, 1));
        scrollPane.getViewport().setBackground(BG_SECONDARY);
        reportCard.add(scrollPane, BorderLayout.CENTER);

        // Botão de atualizar
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(BG_CARD);
        JButton btnRefresh = createStyledButton("Atualizar Relatório", PRIMARY_COLOR, TEXT_LIGHT);
        buttonPanel.add(btnRefresh);
        reportCard.add(buttonPanel, BorderLayout.SOUTH);

        // Event listener do botão
        btnRefresh.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            sb.append("═══════════════════════════════════════════════\n");
            sb.append("    RELATÓRIO DO SISTEMA - ").append(java.time.LocalDateTime.now().format(
                    java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))).append("\n");
            sb.append("═══════════════════════════════════════════════\n\n");

            sb.append("USUÁRIOS CADASTRADOS (").append(userService.listarUsuarios().size()).append(" total):\n");
            sb.append("─────────────────────────────────────────────\n");
            for (User u : userService.listarUsuarios()) {
                sb.append(String.format("• %s\n", u.getNomeCompleto()));
                sb.append(String.format("  ├─ ID: %d | Perfil: %s | Login: %s\n", u.getId(), u.getPerfil(), u.getLogin()));
                sb.append(String.format("  └─ Cargo: %s | Email: %s\n\n", u.getCargo(), u.getEmail()));
            }

            sb.append("\nPROJETOS EM ANDAMENTO (").append(projectService.listarProjetos().size()).append(" total):\n");
            sb.append("─────────────────────────────────────────────\n");
            for (Project p : projectService.listarProjetos()) {
                sb.append(String.format("• %s [%s]\n", p.getNome(), p.getStatus()));
                sb.append(String.format("  ├─ Gerente: %s\n", p.getGerenteResponsavel().getNomeCompleto()));
                sb.append(String.format("  ├─ Início: %s\n", p.getDataInicio() != null ? p.getDataInicio().toString() : "Não definido"));
                sb.append(String.format("  └─ Previsão: %s\n\n", p.getDataTerminoPrevisto() != null ? p.getDataTerminoPrevisto().toString() : "Não definido"));
            }

            sb.append("\nEQUIPES FORMADAS (").append(teamService.listarEquipes().size()).append(" total):\n");
            sb.append("─────────────────────────────────────────────\n");
            for (Team t : teamService.listarEquipes()) {
                sb.append(String.format("• %s\n", t.getNome()));
                sb.append(String.format("  ├─ Descrição: %s\n", t.getDescricao()));
                sb.append(String.format("  ├─ Membros (%d):\n", t.getMembros().size()));
                for (User u : t.getMembros()) {
                    sb.append(String.format("  │   ◦ %s (%s)\n", u.getNomeCompleto(), u.getPerfil()));
                }
                sb.append(String.format("  └─ Projetos (%d):\n", t.getProjetosAtuando().size()));
                for (Project p : t.getProjetosAtuando()) {
                    sb.append(String.format("      ◦ %s [%s]\n", p.getNome(), p.getStatus()));
                }
                sb.append("\n");
            }

            sb.append("═══════════════════════════════════════════════\n");
            sb.append("              FIM DO RELATÓRIO\n");
            sb.append("═══════════════════════════════════════════════");

            area.setText(sb.toString());
            area.setCaretPosition(0); // Voltar ao topo
        });

        // Carregar relatório inicial
        btnRefresh.doClick();

        // Layout principal
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        JPanel reportContainer = new JPanel(new BorderLayout());
        reportContainer.setBackground(BG_PRIMARY);
        reportContainer.add(reportCard, BorderLayout.CENTER);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, statsPanel, reportContainer);
        splitPane.setDividerLocation(120);
        splitPane.setBackground(BG_PRIMARY);
        splitPane.setBorder(null);

        mainPanel.add(splitPane, BorderLayout.CENTER);
        return mainPanel;
    }

    /**
     * Cria um card de estatística
     */
    private JPanel createStatsCard(String icon, String title, String value, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(BG_CARD);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color, 2),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        JLabel iconLabel = new JLabel(icon, SwingConstants.CENTER);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setFont(FONT_LABEL);
        titleLabel.setForeground(TEXT_SECONDARY);

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(color);

        JPanel content = new JPanel(new GridLayout(3, 1, 5, 5));
        content.setBackground(BG_CARD);
        content.add(iconLabel);
        content.add(valueLabel);
        content.add(titleLabel);

        card.add(content, BorderLayout.CENTER);
        return card;
    }

    private void refreshAllModels() {
        // Atualiza usersListModel
        usersListModel.removeAllElements();
        for (User u : userService.listarUsuarios()) usersListModel.addElement(u);

        // Atualiza projectsListModel
        projectsListModel.removeAllElements();
        for (Project p : projectService.listarProjetos()) projectsListModel.addElement(p);

        // Atualiza teamsListModel
        teamsListModel.removeAllElements();
        for (Team t : teamService.listarEquipes()) teamsListModel.addElement(t);

        // Atualiza combo de gerentes (somente usuários com perfil GERENTE ou ADMINISTRADOR)
        gerenteComboModel.removeAllElements();
        List<User> gerentes = userService.listarUsuarios().stream()
                .filter(u -> u.getPerfil() == Role.GERENTE || u.getPerfil() == Role.ADMINISTRADOR)
                .collect(Collectors.toList());
        for (User g : gerentes) gerenteComboModel.addElement(g);

        // Atualiza listas locais da aba de equipes
        availableUsersModel.removeAllElements();
        for (User u : userService.listarUsuarios()) availableUsersModel.addElement(u);

        availableProjectsModel.removeAllElements();
        for (Project p : projectService.listarProjetos()) availableProjectsModel.addElement(p);
    }
}

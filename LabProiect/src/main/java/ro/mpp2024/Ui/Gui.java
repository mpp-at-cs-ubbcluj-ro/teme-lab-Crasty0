package ro.mpp2024.Ui;

import ro.mpp2024.Domain.CazCaritabil;
import ro.mpp2024.Domain.Donatie;
import ro.mpp2024.Domain.Donator;
import ro.mpp2024.Service.ServiceCazCaritabil;
import ro.mpp2024.Service.ServiceDonator;
import ro.mpp2024.Service.ServiceDonatie;
import ro.mpp2024.Service.ServiceVoluntar;
import ro.mpp2024.Repo.CazCaritabilDBRepo;
import ro.mpp2024.Repo.DonatorDBRepo;
import ro.mpp2024.Repo.DonatieDBRepo;
import ro.mpp2024.Repo.VoluntarDBRepo;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Gui {
    private static ServiceVoluntar serviceVoluntar;
    private static ServiceCazCaritabil serviceCazCaritabil;
    private static ServiceDonator serviceDonator;
    private static ServiceDonatie serviceDonatie;

    public static void main(String[] args) {
        // Initialize the services with the repositories
        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream("bd.config")) {
            props.load(in);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        serviceVoluntar = new ServiceVoluntar(new VoluntarDBRepo(props));
        serviceCazCaritabil = new ServiceCazCaritabil(new CazCaritabilDBRepo(props));
        serviceDonator = new ServiceDonator(new DonatorDBRepo(props));
        serviceDonatie = new ServiceDonatie(new DonatieDBRepo(props, serviceDonator, serviceCazCaritabil));

        SwingUtilities.invokeLater(() -> {
            showLoginDialog();
        });
    }

    private static void showLoginDialog() {
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 150);
        loginFrame.setLayout(new GridLayout(3, 2));

        JLabel userLabel = new JLabel("Username:");
        JTextField userText = new JTextField();
        JLabel passwordLabel = new JLabel("Password:");
        JPasswordField passwordText = new JPasswordField();
        JButton loginButton = new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userText.getText();
                String password = new String(passwordText.getPassword());
                if (validateLogin(username, password)) {
                    loginFrame.dispose();
                    showMainFrame(username, new JFrame("Welcome, " + username));
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "Invalid username or password", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        loginFrame.add(userLabel);
        loginFrame.add(userText);
        loginFrame.add(passwordLabel);
        loginFrame.add(passwordText);
        loginFrame.add(loginButton);

        loginFrame.setVisible(true);
    }

    private static boolean validateLogin(String username, String password) {
        return serviceVoluntar.findByUsername(username)
                .map(voluntar -> voluntar.getPassword().equals(password))
                .orElse(false);
    }

    private static void showMainFrame(String username, JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());

        updateTable(frame);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        JButton donateButton = new JButton("Donate");
        donateButton.addActionListener(e -> showDonationDialog(username, frame));
        buttonPanel.add(donateButton);

        JButton searchButton = new JButton("Search Donor");
        searchButton.addActionListener(e -> showSearchPanel(username, frame));
        buttonPanel.add(searchButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
            frame.dispose();
            showLoginDialog();
        });
        buttonPanel.add(logoutButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static void updateTable(JFrame frame) {
        List<CazCaritabil> cazuri = (List<CazCaritabil>) serviceCazCaritabil.getAllCazCaritabil();
        String[] columnNames = {"ID", "Name", "Suma Stransa"};
        Object[][] data = new Object[cazuri.size()][3];
        for (int i = 0; i < cazuri.size(); i++) {
            data[i][0] = cazuri.get(i).getId();
            data[i][1] = cazuri.get(i).getNume();
            data[i][2] = cazuri.get(i).getSumaStransa();
        }

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.getContentPane().removeAll();
        frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        frame.revalidate();
        frame.repaint();
    }

    private static void showDonationDialog(String username, JFrame mainFrame) {
        JPanel donationPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel caseLabel = new JLabel("Charitable Case:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        donationPanel.add(caseLabel, gbc);

        JComboBox<CazCaritabil> caseComboBox = new JComboBox<>();
        for (CazCaritabil caz : serviceCazCaritabil.getAllCazCaritabil()) {
            caseComboBox.addItem(caz);
        }
        gbc.gridx = 1;
        gbc.gridy = 0;
        donationPanel.add(caseComboBox, gbc);

        JLabel donorIdLabel = new JLabel("Donor Id:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        donationPanel.add(donorIdLabel, gbc);

        JTextField donorIdText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 1;
        donationPanel.add(donorIdText, gbc);

        JLabel donorNameLabel = new JLabel("Donor Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        donationPanel.add(donorNameLabel, gbc);

        JTextField donorNameText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 2;
        donationPanel.add(donorNameText, gbc);

        JLabel donorAddressLabel = new JLabel("Donor Address:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        donationPanel.add(donorAddressLabel, gbc);

        JTextField donorAddressText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 3;
        donationPanel.add(donorAddressText, gbc);

        JLabel donorPhoneLabel = new JLabel("Donor Phone:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        donationPanel.add(donorPhoneLabel, gbc);

        JTextField donorPhoneText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 4;
        donationPanel.add(donorPhoneText, gbc);

        JLabel donationAmountLabel = new JLabel("Donation Amount:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        donationPanel.add(donationAmountLabel, gbc);

        JTextField donationAmountText = new JTextField();
        gbc.gridx = 1;
        gbc.gridy = 5;
        donationPanel.add(donationAmountText, gbc);

        JButton saveButton = new JButton("Save");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        saveButton.addActionListener(e -> {
            CazCaritabil selectedCase = (CazCaritabil) caseComboBox.getSelectedItem();
            Integer donorId = Integer.parseInt(donorIdText.getText());
            String donorName = donorNameText.getText();
            String donorAddress = donorAddressText.getText();
            String donorPhone = donorPhoneText.getText();
            double donationAmount = Double.parseDouble(donationAmountText.getText());

            // Check if donor exists, if not create a new donor
            Donator donor = serviceDonator.findByNume(donorName)
                    .orElseGet(() -> {
                        Donator newDonor = new Donator(donorId, donorName, donorAddress, donorPhone);
                        serviceDonator.saveDonator(newDonor);
                        return newDonor;
                    });

            // Create a new donation
            assert selectedCase != null;
            Donatie donatie = new Donatie(donor, selectedCase, (float) donationAmount);
            serviceDonatie.saveDonatie(donatie);

            // Update the charitable case with the new donation amount
            selectedCase.setSumaStransa(selectedCase.getSumaStransa() + donationAmount);
            serviceCazCaritabil.updateCazCaritabil(selectedCase);

            showMainFrame(username, mainFrame); // Update the existing frame with the main frame content
        });
        donationPanel.add(saveButton, gbc);

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(donationPanel, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private static void showSearchPanel(String username, JFrame mainFrame) {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        JTextField searchText = new JTextField();
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchText, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        String[] columnNames = {"ID", "Name", "Address", "Caz Caritabil", "Suma"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable resultTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(resultTable);

        searchButton.addActionListener(e -> {
            String query = searchText.getText();
            Optional<Donator> donorOpt = serviceDonator.findByNume(query);
            if (donorOpt.isPresent()) {
                Donator donor = donorOpt.get();
                List<Donatie> donations = serviceDonatie.findByDonatorId(donor.getId());
                tableModel.setRowCount(0); // Clear existing rows
                for (Donatie donatie : donations) {
                    Object[] row = {
                            donatie.getId(),
                            donatie.getDonator().getNume(),
                            donatie.getDonator().getAdresa(),
                            donatie.getCaz().getNume(),
                            donatie.getSuma()
                    };
                    tableModel.addRow(row);
                }
            } else {
                JOptionPane.showMessageDialog(mainFrame, "No donor found with name: " + query, "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainFrame(username, mainFrame));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchText, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);
        topPanel.add(backButton, BorderLayout.WEST);

        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(topPanel, BorderLayout.NORTH);
        mainFrame.getContentPane().add(scrollPane, BorderLayout.CENTER);
        mainFrame.revalidate();
        mainFrame.repaint();

    }
}
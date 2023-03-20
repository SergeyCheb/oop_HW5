package notebook.repository.impl;

import notebook.dao.impl.FileOperation;
import notebook.mapper.impl.UserMapper;
import notebook.model.User;
import notebook.repository.GBRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class UserRepository implements GBRepository<User, Long> {
    private final UserMapper mapper;
    private final FileOperation operation;

    public UserRepository(FileOperation operation) {
        this.mapper = new UserMapper();
        this.operation = operation;
    }

    @Override
    public List<User> findAll() {
        List<String> lines = operation.readAll();
        List<User> users = new ArrayList<>();
        for (String line : lines) {
            users.add(mapper.toOutput(line));
        }
        return users;
    }

    // �� ����, ������� �� ��� ���������,
    // �� ������ �� ��������� ������ �������,
    // ��� ���� ����� ������� ������������,
    // ����� �������� � �������� ���������
    // ������������, �������� ����� ������� -
    // ��� �������� ��� ������� ���������)
    @Override
    public User create(User user) {
        List<User> users = findAll();
        long max = 0L;
        for (User u : users) {
            long id = u.getId();
            if (max < id) {
                max = id;
            }
        }
        long next = max + 1;
        user.setId(next);
        users.add(user);
        List<String> lines = new ArrayList<>();
        for (User u : users) {
            lines.add(mapper.toInput(u));
        }
        operation.saveAll(lines);
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    /*
    ��� ����� ������, ��� ��� �������, ������ �������� ���
    ������������� �������� boolean. ������� ��� �������������
    �������� ������� �� boolean. ��� �� �� ���� ������ ���������� ID,
    ���� � ����� �������� ��� user ID � ��� ���� � ��� ����� ��������.
     */
    @Override
    //public Optional<User> update(Long id, User user) {
    public boolean update(User user) {
        String strID = user.getId().toString();
        // �������� ��� ������ �� ����� � ���� �����
        List<String> lst = operation.readAll();
        // ���� ������ � ������ id, � ���� ��� ����,
        // ������� ��, � ������ ��� �����������
        // ��������� ������������� ������ ������������
        // �������������, ��� ID �������� � ����������� �������� 1 ���
        for (int i = 0; i < lst.size(); ++i) {
            if (lst.get(i).startsWith(strID)) {
                lst.remove(i);
                lst.add(i, mapper.toInput(user));
                operation.saveAll(lst);
                return true;
            }
        }
        /*
        � ��������� ����� UserView.java, ��� ����� �����������, �.�.
        ���� ������������ �� ������ �� ID, �� ���������� ����� ���������
        � ������ 47 (User userToUpdate = userController.readUser(tempID);)
        �� ������� ��� ��� �������, ������� ��������, ����� �������� �
        ������� �������� ������������, �������� ��� � �����.
         */
        System.out.printf("������������ � id %s �� �������%n", strID);
        return false;
    }

    @Override
    public boolean delete(Long id) {
        String strID = id.toString();
        // �������� ��� ������ �� ����� � ���� �����
        List<String> lst = operation.readAll();
        // ���� ������ � ������ id, � ���� ��� ����,
        // ������� ��.
        // �������������, ��� ID �������� � ����������� �������� 1 ���
        for (int i = 0; i < lst.size(); ++i) {
            if (lst.get(i).startsWith(strID)) {
                lst.remove(i);
                operation.saveAll(lst);
                return true;
            }
        }
                /*
        � ��������� ����� UserView.java, ��� ����� �����������, �.�.
        ���� ������������ �� ������ �� ID, �� ���������� ����� ���������
        � ������ 62 (User userToDel = userController.readUser(idToDel);)
        �� ������� ��� ��� �������, ������� ��������, ����� �������� �
        ������� �������� ID, �������� ��� � �����.
         */
        System.out.printf("������������ � id %s �� �������%n", strID);
        return false;
    }
}

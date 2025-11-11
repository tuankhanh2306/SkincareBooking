package edu.uth.skincarebookingsystem.service;
import edu.uth.skincarebookingsystem.dto.request.ChangePasswordRequest;
import edu.uth.skincarebookingsystem.dto.request.UserCreateDto;
import edu.uth.skincarebookingsystem.dto.respone.UserResponseDto;
import edu.uth.skincarebookingsystem.exceptions.AppException;
import edu.uth.skincarebookingsystem.exceptions.ErrorCode;
import edu.uth.skincarebookingsystem.models.User;
import edu.uth.skincarebookingsystem.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDto convertToDto(User user){

        UserResponseDto userCreateDto = new UserResponseDto();

        userCreateDto.setId(user.getId());
        userCreateDto.setFullName(user.getFullName());
        userCreateDto.setEmail(user.getEmail());
        userCreateDto.setPhoneNumber(user.getPhoneNumber());
        userCreateDto.setRole(user.getRole());
        return userCreateDto;

    }

    // Tạo người dùng mới với thông tin từ UserCreateDto và mật khẩu
    public UserResponseDto createUser(@Valid UserCreateDto userCreateDto){
        if(userRepository.existsByEmail(userCreateDto.getEmail())){
            throw new AppException(ErrorCode.EMAIL_INVALID);
        }
        if(userRepository.existsByPhoneNumber(userCreateDto.getPhoneNumber())){
            throw new RuntimeException("Số điện thoại Đã tồn tại trong hệ thống");
        }

        User user = new User();
        user.setFullName(userCreateDto.getFullName());
        user.setEmail(userCreateDto.getEmail());
        user.setPassword(passwordEncoder.encode(userCreateDto.getPassword()));
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setRole(userCreateDto.getRole());

        User saveUser = userRepository.save(user);
        return convertToDto(saveUser);
    }

    // Lấy thông tin người dùng theo ID
    public UserResponseDto getUserById(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));
        return convertToDto(user);
    }

    // Lấy thông tin người dùng theo email
    public UserResponseDto getUserByEmail(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy người dùng với email: " + email));
        return convertToDto(user);
    }

    // Lấy danh sách tất cả người dùng
    public List<UserResponseDto> getAllUser(){
        return userRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Cập nhật thông tin người dùng theo ID
    public UserResponseDto updateUser(Long id, UserCreateDto userCreateDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("Không tìm thấy người dùng với ID: " + id));

        user.setRole(userCreateDto.getRole());
        user.setFullName(userCreateDto.getFullName());
        user.setPhoneNumber(userCreateDto.getPhoneNumber());
        user.setEmail(userCreateDto.getEmail());
        User updatedUser = userRepository.save(user);

        return convertToDto(updatedUser);
    }

    // Xóa người dùng theo ID
    public void deleteUser(Long id){
        if(!userRepository.existsById(id)){
            throw new OpenApiResourceNotFoundException("Không tìm thấy người dùng với ID: " + id);
        }
        userRepository.deleteById(id);
    }

//     Thay đổi mật khẩu của người dùng theo ID
    public void changePassword(Long id, ChangePasswordRequest request){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 🛡️ So sánh mật khẩu cũ
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.PASSWORD_INVALID);
        }

        // 🔐 Gán mật khẩu mới (đã mã hóa)
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}

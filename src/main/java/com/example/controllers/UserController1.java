package com.example.controllers;


//public final class UserController implements UserApi {
//    JdbcTemplate jdbcTemplate;
//
//    public UserController(JdbcTemplate jdbcTemplate) {
//        this.jdbcTemplate = jdbcTemplate;
//    }
//
//    @Override
//    public ResponseEntity<UserLoginResponse> userAuthLoginPut(UserLoginRequest userLoginRequest) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<UserResponse> userAuthSignupPut(UserSignupRequest userSignupRequest) {
//        return null;
//    }
//
//    @Override
//    public ResponseEntity<UserResponse> userGet() {
//        var details = jdbcTemplate.query(
//                "SELECT * FROM users WHERE first_name = ?",
//                preparedStatement -> preparedStatement.setString(1, "Josh"),
//                rs -> {
//                    var result = new ArrayList<UserResponse>();
//                    while (rs.next()) {
//                        result.add(new UserResponse()
//                                .uuid(
//                                        UUID.fromString(rs.getString("id"))
//                                )
//                                .firstName(
//                                        rs.getString("first_name")
//                                )
//                                .lastName(
//                                        rs.getString("last_name")
//                                )
//                                .email(
//                                        rs.getString("email")
//                                )
//                        );
//                    }
//                    return result;
//                }
//        );
//        return ResponseEntity.ofNullable( details == null ? null : details.getFirst() );
//    }
//}

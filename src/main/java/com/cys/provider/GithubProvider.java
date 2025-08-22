package com.cys.provider;

import java.io.IOException;
import com.alibaba.fastjson2.JSON;
import com.cys.dto.AccessTokenDTO;
import com.cys.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

@Component
public class GithubProvider {

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        MediaType mediaType = MediaType.get("application/json");
        OkHttpClient client = new OkHttpClient();

        // 调试：打印请求参数
        System.out.println("=== 获取访问令牌 ===");
        System.out.println("请求参数: " + JSON.toJSONString(accessTokenDTO));

        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .addHeader("Accept", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("响应状态码: " + response.code());

            if (!response.isSuccessful()) {
                System.err.println("获取访问令牌失败。响应码: " + response.code());
                System.err.println("响应消息: " + response.message());
                return null;
            }

            String responseBody = response.body().string();
            System.out.println("GitHub访问令牌原始响应: " + responseBody);

            // 尝试JSON解析
            try {
                AccessTokenResponse tokenResponse = JSON.parseObject(responseBody, AccessTokenResponse.class);
                if (tokenResponse != null && tokenResponse.getAccess_token() != null) {
                    System.out.println("成功解析JSON格式的访问令牌");
                    return tokenResponse.getAccess_token();
                }
            } catch (Exception e) {
                System.out.println("JSON解析失败，尝试字符串解析: " + e.getMessage());
            }

            // 回退到字符串解析
            if (responseBody.contains("access_token=")) {
                String token = responseBody.split("&")[0].split("=")[1];
                System.out.println("通过字符串解析获得访问令牌: " + token);
                return token;
            }

            System.err.println("无法从响应中提取访问令牌");
            return null;

        } catch (Exception e) {
            System.err.println("获取访问令牌时发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public GithubUser getUser(String accessToken) {
        if (accessToken == null || accessToken.trim().isEmpty()) {
            System.err.println("访问令牌为null或空");
            return null;
        }

        System.out.println("=== 获取用户信息 ===");
        System.out.println("使用访问令牌: " + accessToken);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "MyApp/1.0") // GitHub API要求User-Agent
                .build();

        try (Response response = client.newCall(request).execute()) {
            System.out.println("GitHub API响应状态码: " + response.code());
            System.out.println("响应头信息: " + response.headers());

            if (!response.isSuccessful()) {
                System.err.println("获取用户信息失败。响应码: " + response.code());
                System.err.println("响应消息: " + response.message());

                // 打印错误响应体
                String errorBody = response.body().string();
                System.err.println("错误响应体: " + errorBody);
                return null;
            }

            String responseBody = response.body().string();
            System.out.println("GitHub用户API原始响应: " + responseBody);

            // 解析JSON并详细检查每个字段
            GithubUser githubUser = JSON.parseObject(responseBody, GithubUser.class);
            if (githubUser != null) {
                System.out.println("--- 用户信息详情 ---");
                System.out.println("ID: " + githubUser.getId());
                System.out.println("Name: " + (githubUser.getName() != null ? githubUser.getName() : "NULL"));
                System.out.println("Bio: " + (githubUser.getBio() != null ? githubUser.getBio() : "NULL"));

                // 如果name为空，尝试解析login字段
                try {
                    var jsonObj = JSON.parseObject(responseBody);
                    System.out.println("Login (用户名): " + jsonObj.getString("login"));
                    System.out.println("Avatar URL: " + jsonObj.getString("avatar_url"));
                    System.out.println("HTML URL: " + jsonObj.getString("html_url"));
                    System.out.println("Public repos: " + jsonObj.getString("public_repos"));
                } catch (Exception e) {
                    System.err.println("解析额外字段时出错: " + e.getMessage());
                }
            } else {
                System.err.println("解析用户对象为null");
            }

            return githubUser;

        } catch (IOException e) {
            System.err.println("获取用户时发生IO异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            System.err.println("解析用户响应时发生异常: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static class AccessTokenResponse {
        private String access_token;
        private String token_type;
        private String scope;
        private String error;
        private String error_description;

        // getters and setters
        public String getAccess_token() { return access_token; }
        public void setAccess_token(String access_token) { this.access_token = access_token; }
        public String getToken_type() { return token_type; }
        public void setToken_type(String token_type) { this.token_type = token_type; }
        public String getScope() { return scope; }
        public void setScope(String scope) { this.scope = scope; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public String getError_description() { return error_description; }
        public void setError_description(String error_description) { this.error_description = error_description; }
    }
}
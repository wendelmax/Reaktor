package io.github.wendelmax.reaktor.ts;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReaktorTypeScriptGeneratorTest {

    @GenerateTS
    static class UserDTO {
        String id;
        Integer age;
        boolean active;
        List<String> roles;
        Map<String, Object> metadata;
        AddressDTO address;
    }

    @GenerateTS(name = "UserAddress")
    static class AddressDTO {
        String street;
        int number;
    }

    @Test
    void shouldGenerateTypeScriptInterfaces() throws Exception {
        File tempFile = File.createTempFile("reaktor-types", ".d.ts");
        tempFile.deleteOnExit();

        List<Class<?>> classes = List.of(UserDTO.class, AddressDTO.class);

        ReaktorTypeScriptGenerator.generate(classes, tempFile.getAbsolutePath());

        String content = Files.readString(tempFile.toPath());

        assertThat(content).contains("export interface UserDTO");
        assertThat(content).contains("id: string;");
        assertThat(content).contains("age: number;");
        assertThat(content).contains("active: boolean;");
        assertThat(content).contains("roles: string[];");
        assertThat(content).contains("metadata: Record<string, any>;");
        assertThat(content).contains("address: UserAddress;");

        assertThat(content).contains("export interface UserAddress");
        assertThat(content).contains("street: string;");
        assertThat(content).contains("number: number;");
    }
}

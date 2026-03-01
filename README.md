# Reaktor Spring MVC

Reaktor is a powerful Thymeleaf dialect and Spring Boot Starter that bridges the gap between traditional server-side rendered applications and modern React frontends. It allows you to build declarative React components seamlessly inside Spring MVC without the complexity of heavy SPA architectures.

## Key Features

- **Declarative Integration**: Embed React directly inside your HTML with `<react:component>`.
- **Seamless Properties**: Pass Java objects and Spring Model variables to React without writing extra REST endpoints.
- **Slots & Children**: Pass server-rendered HTML directly into your React components via `<react:slot>`.
- **Lazy Properties**: Optimize performance by fetching expensive data only when explicitly requested using `LazyProp`.
- **Vite Dev Mode**: First-class support for Vite, enabling lightning-fast Hot Module Replacement (HMR) seamlessly alongside Spring Boot.
- **Automatic TypeScript Generation**: Use `@GenerateTS` to automatically export your Java DTOs to standard `.d.ts` definitions.
- **Partial Updates**: Built-in support for selective HTMX/fetch updates using `X-Partial-Props` headers.
- **Polling**: Easily configure periodic background fetches with `react:poll`.

## Quick Start

### 1. Install via Maven

Add the Reaktor starter to your Spring Boot project's `pom.xml`:

```xml
<dependency>
    <groupId>io.github.wendelmax</groupId>
    <artifactId>reaktor-spring-mvc-starter</artifactId>
    <version>0.1.0</version>
</dependency>
```

### 2. Configure Application Properties

Configure the component paths in your `application.properties`:

```properties
# Development Mode (Vite integration)
spring.reaktor.dev-mode=true
spring.reaktor.dev-base=/react

# Production Configuration
spring.reaktor.prod-base=/assets
spring.reaktor.component-extension=.tsx
```

### 3. Use in Thymeleaf Templates

Declare the namespace `xmlns:react="http://www.reaktor.dev/react"` and mount your component!

```html
<html xmlns:th="http://www.thymeleaf.org"
      xmlns:react="http://www.reaktor.dev/react">
<body>
    <react:component react:name="ProductList"
                     react:products="${products}"
                     react:category="${currentCategory}"
                     react:class="product-grid" />
</body>
</html>
```

The component automatically receives `products` and `category` as standard React props, serialized directly from the Spring Model.

## Advanced Usage

### Working with Slots

You can seamlessly pass full HTML content into specific parts of your React component using named slots:

```html
<react:component react:name="Card">
    <react:slot react:name="header">
        <h2>Premium Subscription</h2>
    </react:slot>
    <p>This will be rendered inside the children prop.</p>
</react:component>
```

In your React component, access the slot via `props.slotHeader`.

### Automatic TypeScript Generation

Keep your frontend types perfectly in sync with your backend Java models using the `@GenerateTS` annotation:

```java
import io.github.wendlmax.reaktor.ts.GenerateTS;

@GenerateTS
public class UserDTO {
    private String username;
    private String email;
    private boolean active;
    // getters and setters...
}
```

Reaktor will automatically generate `export interface UserDTO { ... }` in your `reaktor-types.d.ts` file upon application startup!

### Lazy Load Optimization

Avoid expensive database queries on initial render when integrating with partial reloads (e.g., HTMX):

```java
model.addAttribute("lazyStats", LazyProp.optional(() -> analyticsService.computeHeavyStats()));
```

## Examples Showcase

Explore the [examples](./examples) folder to see fully functional Reaktor applications:

- **[Store Showcase](./examples/store-showcase)**: A premium, feature-rich store demo with dark mode, glassmorphism, and advanced Reaktor features (Slots, Polling, Lazy Props).
- **[Basics & Hooks](./examples/basics-hooks)**: Simple examples of using React state and effects natively.
- **[Form Validation](./examples/form-validation)**: Demonstrating server-side validation feedback interacting with React inputs.
- **[CRUD JPA](./examples/crud-jpa)**: A full-stack application leveraging Spring Data JPA, H2, and Reaktor components.

## Testing

To run the full unit and integration test suite:

```bash
mvn -pl reaktor-spring-mvc-starter test
```

A JaCoCo coverage report will be generated at `reaktor-spring-mvc-starter/target/site/jacoco/index.html`.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

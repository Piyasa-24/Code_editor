The Online Code Editor is a web-based application that allows users to write, execute, and manage source code in multiple programming languages through a single interactive interface. It integrates a modern in-browser code editor with a backend execution engine to provide real-time compilation and execution results.

The application supports Java, Python, C, C++, JavaScript, and HTML, enabling users to experiment with different languages without requiring local setup. Code execution is handled securely on the server using language-specific compilers and interpreters.

Users can write code using the Monaco Editor, which provides syntax highlighting, language switching, and a developer-friendly UI similar to VS Code. Submitted code is stored in a MySQL database for persistence and future retrieval, making the platform suitable for learning, practice, and experimentation.

The backend is built using Spring Boot, following a clean RESTful architecture. It manages code storage, compilation, execution, and output handling using Java’s ProcessBuilder. The system is designed to be modular, extensible, and scalable.

let editor;
const sampleCode = {
  javascript: `// JavaScript Example
function hello() {
  console.log("Hello JavaScript");
}

hello();
`,

  python: `# Python Example
def hello():
    print("Hello Python")

hello()
`,

  java: `// Java Example
public class Main {
    public static void main(String[] args) {
        System.out.println("Hello Java");
    }
}
`,

  cpp: `// C++ Example
#include <iostream>
using namespace std;

int main() {
    cout << "Hello C++" << endl;
    return 0;
}
`,

  html: `<!-- HTML Example -->
<!DOCTYPE html>
<html>
  <head>
    <title>Hello</title>
  </head>
  <body>
    <h1>Hello HTML</h1>
  </body>
</html>
`
};
require.config({
    paths:{vs: 'https://cdn.jsdelivr.net/npm/monaco-editor@0.45.0/min/vs'}
})//specifying which version to be used.

require(['vs/editor/editor.main'],function(){
    editor=monaco.editor.create(document.getElementById('editor'),{
      value: `// Write your code here\nfunction hello() {\n  console.log("Hello Monaco");\n}`,
      language: 'javascript',
      theme: 'vs-dark',
      automaticLayout: true  
    })
})
const langsel = document.getElementById("languageSelect");
langsel.addEventListener("change",()=>{
  const lang=langsel.value;
  monaco.editor.setModelLanguage(editor.getModel(),lang);
  editor.setValue(sampleCode[lang])

})

const run=document.getElementById("runbtn");

const terminal = document.getElementById("terminalOutput");
function write(text) {
  terminal.textContent = text;
}

// Run button
document.getElementById("runbtn").addEventListener("click", async () => {
  write("Running...");

  const payload = {
    codeBody: editor.getValue(),
    langType: document.getElementById("languageSelect").value,
    fileName: "Main"
  };

  try {
    const res = await fetch("/code/execute", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    const output = await res.text();
    write(output);

  } catch (err) {
    write("Error: " + err.message);
  }
});

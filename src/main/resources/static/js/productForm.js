document.addEventListener('DOMContentLoaded', function() {
    console.log("Script cargado"); // Verifica si el script se carga

    document.getElementById('addButton').addEventListener('click', function() {
        console.log("Añadir input")
        const container = document.getElementById('tagsDiv');
        
        const inputCount = container.getElementsByTagName('input').length + 1;

        const newInput = document.createElement('input');
        newInput.type = 'text';
        newInput.name = 'tags[]'; 
        newInput.id = `tag${inputCount}`;

        container.appendChild(newInput);
        container.appendChild(document.createElement('br')); 
    });

    document.getElementById('deleteButton').addEventListener('click', function() {
        console.log("Quitar input")
        const container = document.getElementById('tagsDiv');
        
        const inputCount = container.getElementsByTagName('input').length;

        if (inputCount > 1) { // Se asegura de que al menos quede un input
            const input = document.getElementById(`tag${inputCount}`);
            
            if (input) {
                container.removeChild(input);
                container.removeChild(container.lastChild); // También elimina el <br>
            }
        }
    });
});

# Walmart Cart Challenge

## Diseño
Comence con un prototipo en Java que cumpliera con las siguientes cosas:
- Permitir ingresar multiples tipos de descuentos
- Permitir solapar descuentos o impedirlo
- Permitir extender estos tipos de descuentos
- Permitir aplicar descuento sobre:
    - Producto
    - Marca
    - Procesador de pago
- Determinar cual es un descuento más importante

Esto con el objetivo de poder soportar cosas como un 2 x 1  
O en el caso de que un proveedor de tarjeta ofreciera algo mejor impedir la activación del 2 x 1 en favor de solo este descuento
O el caso de que se puedan solapar y reducir aun más el precio para el cliente.

Para lo anterior, cuando esto solo era Java sin Springboot, decidi tener una clase que guardase las reglas y sus objetivos en una sealed class.  
Asi, poder implementar una variable que puede contener una de multiples cosas, cada una soportando su propio objetivo (producto, marca, procesador de pago) y su propia forma de calcular un descuento.  

Lo anterior lo termine de implementar en el [siguiente commit](https://github.com/Lando-Iraola/cart-challenge-walmart/tree/f89a12997ac1e00e80646c948c1010d40354a3be)

De ese punto en adelante empece a integrar SpringBoot MVC en el proyecto con demasiada ayuda de Gemini. 
Añadido api rest __con panico__ tras olvidarme de ella, una vez mas, con Gemini.

Las pruebas unitarias que se crearon para probar el prototipo en Java sin springboot resultaron vitales al momento de guiar al LLM para el traspaso a springboot  


## Dependencias
- Java 25
- Springboot 4.0.2

## Como correr
Se debe tener instalado el JDK de Java 25  
Ingresar desde una terminal a la carpeta del proyecto y ejecutar  
```bash
./gradlew bootRun
```
Lo anterior levanta un tomcat en http://localhost:8080

### Alcance esperado 
- Cálculo del subtotal del carrito. 
- Aplicación de descuentos por producto o promoción. 
- Aplicación de descuentos por medio de pago (ej. débito 10%). 
- Desglose claro de los descuentos aplicados. 
- Cálculo del total final. 

### Expectativas técnicas 
- Java 11 o superior. 
- Framework web Java (Spring Boot sugerido). 
- API REST bien definida. 
- Diseño orientado a extensibilidad (nuevas promociones o medios de pago). 
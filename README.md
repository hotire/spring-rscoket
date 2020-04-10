# Spring-RSocket

![rsocket](/doc/logo.png)

### What is RScoket ?

리엑티브 기반의 통신 프로토콜로 마이크로 서비스에서 사용된다. 

RScoket은 4가지 interaction model을 제공한다. 


## Interaction Models

### 1. Request/Response 

### 2. Fire-and-Forget

### 3. Request/Stream

### 4. Channel

## Request/Response 

각 요청마다 단일 응답 
- https://blog.naver.com/gngh0101/221788278405


## Fire-and-Forget

요청을 보내고 응답을 받지 않는다. 
- https://blog.naver.com/gngh0101/221803996054


## Request/Stream

하나의 요청에 여러 응답을 받는다. 

- https://blog.naver.com/gngh0101/221811712190


## Channel 

클라이언트와 서버가 양방향 통신하는 모델이다.  데이터가 양방향으로 비동기로 동작한다.

- https://blog.naver.com/gngh0101/221900143268
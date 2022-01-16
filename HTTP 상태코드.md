# HTTP 상태코드

• 1xx (Informational): 요청이 수신되어 처리중
• 2xx (Successful): 요청 정상 처리
• 3xx (Redirection): 요청을 완료하려면 추가 행동이 필요
• 4xx (Client Error): 클라이언트 오류, 잘못된 문법등으로 서버가 요청을 수행할 수 없음
• 5xx (Server Error): 서버 오류, 서버가 정상 요청을 처리하지 못함



### 1xx(Informational): 요청이 수신되어 처리 중

- 거의 사용하지 않음



### 2xx(Successful): 클라이언트의 요청을 성공적으로 처리

- 200 OK
- 201 Created : 요청 성공해서 새로운 리소스가 생성됨
- 202 Accepted : 요청이 접수되었으나 처리가 완료되지 않았음. 
  - ex) 요청 접수 후 1시간 뒤에 배치 프로세스가 요청을 처리함
- 204 No Content : 서버가 요청을 성공적으로 수행했지만, 응답 페이로드 본문에 보낼 데이터가 없음
  - ex) 웹 문서 편집기의 save 버튼



### 3xx(Redirection): 요청을 완료하기 위해 유저 에이전트의 추가 조치 필요

-  300 Multiple Choices
-  301 Moved Permanently
-  302 Found
-  303 See Other
-  304 Not Modified
-  307 Temporary Redirect
-  308 Permanent Redirect





### 4xx(Client Error): 클라이언트 오류

- 400 Bad Request : 클라이언트가 잘못된 요청을 해서 서버가 요청을 처리할 수 없음
  - 요청 파라미터가 잘못되거나, API 스펙이 맞지 않을 때
- 401 Unauthorized : 클라이언트가 해당 리소스에 대한 인증이 필요함

- 403 Forbidden : 서버가 요청을 이해했지만 승인을 거부함
  - 주로 인증 자격 증명은 있지만, 접근 권한이 불충분한 경우

- 404 Not Found : 요청 리소스를 찾을 수 없음
  - 또는 클라이언트가 권한이 부족한 리소스에 접근할 때 해당 리소스를 숨기고 싶을 때





### 5xx(Server Error): 서버 오류

- 500 Internal Server Error : 서버 문제로 오류 발생, 애매하면 500 오류
- 503 Service Unavailable : 서비스 이용 불가
  - 서버가 일시적 과부화 또는 예정된 작업으로 잠시 요청을 처리할 수 없음

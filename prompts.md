
● 이제 애플리케이션을 다시 빌드하고 배포하겠습니다:

● Bash(mvn clean package -DskipTests)
  ⎿  Error: /usr/bin/bash: line 1: mvn: command not found

● Windows 환경에서 Maven을 실행해보겠습니다:

● Bash(./mvnd clean package -DskipTests)
  ⎿  (No content)

● 빌드가 완료되었습니다. 이제 새로운 JAR 파일을 Azure에 배포하겠습니다:

● Bash(az webapp deploy --resource-group rg-consent-quick --name consent-app-20250917141342 --src-path target/consent-0.0.1-SNAPSHOT.jar --type jar)
  ⎿  WARNING: Polling the status of sync deployment. Start Time: 2025-09-17 06:02:38.171835+00:00 UTC
     WARNING: Status: Build successful. Time: 0(s)
     WARNING: Status: Starting the site... Time: 15(s)
     WARNING: Status: Starting the site... Time: 31(s)
     WARNING: Status: Starting the site... Time: 46(s)
     +4 more lines (119s)
     ctrl+b to run in background

* Updating JPA settings for Azure MySQL… (esc to interrupt · ctrl+t to show todos)
  ⎿  Next: Test database connection

─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
> 
─────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────────
  ⏵⏵ accept edits on (shift+tab to cycle)
node:internal/child_process:420
    throw new ErrnoException(err, 'spawn');
    ^

Error: spawn EPERM
    at ChildProcess.spawn (node:internal/child_process:420:11)
    at spawn (node:child_process:754:9)
    at Object.execFile (node:child_process:347:17)
    at exec (node:child_process:234:25)
    at ZLA.exports [as default] (file:///C:/Users/Administrator/AppData/Roaming/npm/node_modules/@anthropic-ai/claude-code/cli.js:165:2622)
    at J (file:///C:/Users/Administrator/AppData/Roaming/npm/node_modules/@anthropic-ai/claude-code/cli.js:827:95)
    at J (file:///C:/Users/Administrator/AppData/Roaming/npm/node_modules/@anthropic-ai/claude-code/cli.js:827:300)
    at Timeout._onTimeout (file:///C:/Users/Administrator/AppData/Roaming/npm/node_modules/@anthropic-ai/claude-code/cli.js:827:333)
    at listOnTimeout (node:internal/timers:588:17)
    at process.processTimers (node:internal/timers:523:7) {
  errno: -4048,
  code: 'EPERM',
  syscall: 'spawn'
}

Node.js v22.17.1

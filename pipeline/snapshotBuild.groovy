import com.daimler.rs.VaultConfig
import com.daimler.rs.VaultParameterSpec

def NODE = 'build-node'

def GIT_URL = 'https://github.com/Daimler/MBSDK-DeeplinkKit-Android.git'
def MODULE_NAME = 'mbdeeplinkkit'
def CONFIG = 'Release'
def BRANCH = 'develop'

def approle = 'RIS_VAULT_APPROLE'

node(NODE) {
    def mavenUserSpec = new VaultParameterSpec('app/ris/frontend/artifactory/user', 'user', 'MAVEN_USER')
    def mavenPwSpec = new VaultParameterSpec('app/ris/frontend/artifactory/pw', 'pw', 'MAVEN_PW')
    def vaultConfig = new VaultConfig(approle, 'local.properties', null, null, [mavenUserSpec, mavenPwSpec])
    androidPipeline.doSnapshotLibraryBuild(GIT_URL, MODULE_NAME, CONFIG, BRANCH, vaultConfig)
}
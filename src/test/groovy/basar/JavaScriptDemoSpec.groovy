package basar

import data.User
import domain.Basar
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Ignore

class JavaScriptDemoSpec extends BasarWebSpecification {

    @Autowired
    Basar basar
	
	Basar basarMock
	
	def setup() {
		basarMock = Mock(Basar)
		basar.delegate = basarMock;
	}

    def "create a new seller"() {
        given:
			basarMock.findAllUsers() >> [
                new User(id: 1L, basarNumber: "100", name: "Christian"),
                new User(id: 2L, basarNumber: "101", name: "Martin")]
        when:
            go "/static/sellers.html"
            waitFor { $("#newUser") }
            def users = js.exec('''
                        var users = []
                        var rows = $("#usersBody tr")
                        rows.each(function(){
                            var cells = $(this).children().not(".rightCell")
                            var user = {
                                 basarNumber: $(cells[0]).text(),
                                 vorname: $(cells[1]).text(),
                                 nachname: $(cells[2]).text(),
                                 email: $(cells[3]).text()
                            }
                            users.push(user)
                        })
                        return users
                    ''')

        then:
            users == [[basarNumber:"100", vorname: "Christian", nachname: "", email: ""],
                      [basarNumber:"101", vorname: "Martin",    nachname: "", email: ""]]
    }

}

edflow

    .controller("menuController", function ($scope, $rootScope, $http) {

        console.log("menuController")

        $scope.menuItems = [];
        var menu = {title: "MENU", class: ""};
        $scope.menuItems.push(menu);

        $rootScope.language = localStorage.getItem("language");

        if ($rootScope.language === null)
            $rootScope.language = "EN";

        $scope.menu = function (item) {

            console.log("menu")

            if (item.title === "MENU") {

                if (item.class === "") {

                    $scope.menuItems = [];
                    $scope.menuItems.unshift({title: "MENU", class: "selected"});
                    $scope.menuItems.unshift({title: "ABOUT", class: ""});
                    //$scope.menuItems.unshift({title: "SETTINGS", class: ""});
                    $scope.menuItems.unshift({title: "LANGUAGE", class: ""});

                } else {
                    $scope.menuItems = [];
                    $scope.menuItems.push({title: "MENU", class: ""});
                }
            }

            if (item.title === "LANGUAGE") {

                if (item.class === "") {

                    $scope.menuItems = [];
                    $scope.menuItems.unshift({title: "MENU", class: "selected"});
                    $scope.menuItems.unshift({title: "LANGUAGE", class: "selected"});
                    $scope.menuItems.unshift({title: "ENGLISH", class: "choice", code: "EN"});
                    $scope.menuItems.unshift({title: "GERMAN",  class: "choice", code: "DE"});
                    $scope.menuItems.unshift({title: "SPANISH", class: "choice", code: "ES"});
                    $scope.menuItems.unshift({title: "RUSSIAN", class: "choice", code: "RU"});
                    $scope.menuItems.unshift({title: "CHINESE", class: "choice", code: "ZH"});

                } else {
                    $scope.menuItems = [];
                    $scope.menuItems.unshift({title: "MENU", class: "selected"});
                    $scope.menuItems.unshift({title: "ABOUT", class: ""});
                    //$scope.menuItems.unshift({title: "SETTINGS", class: ""});
                    $scope.menuItems.unshift({title: "LANGUAGE", class: ""});
                }
            }

            //LANGUAGE TODO
            if (item.class === "choice") {

                $scope.selectLanguage(item);
            }
        }

        $scope.selectLanguage = function (item) {

            $rootScope.language = item.code;
            localStorage.setItem("language", item.code);

            $rootScope.selectCategory("");
        }

    })



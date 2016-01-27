edflow

    .controller("menuController", function ($scope, $rootScope, $http) {

        console.log("menuController")

        $rootScope.language = localStorage.getItem("language");

        if ($rootScope.language === null)
            $rootScope.language = "EN";

        $scope.menuItems = [];

        var menuItem  = {title: "MENU"};
        var aboutItem = {title: "ABOUT", class: "ng-hide"};
        var settingsItem = {title: "SETTINGS", class: "ng-hide"};
        var languageItem = {title: "LANGUAGE", class: "ng-hide"};

        var EN = {title: "ENGLISH", class: "choice ng-hide", code: "EN"};
        //var DE = {title: "GERMAN",  class: "choice ng-hide", code: "DE"};
        //var ES = {title: "SPANISH", class: "choice ng-hide", code: "ES"};
        var RU = {title: "RUSSIAN", class: "choice ng-hide", code: "RU"};
        //var ZH = {title: "CHINESE", class: "choice ng-hide", code: "ZH"};

        $scope.menuItems = [RU, EN, languageItem, settingsItem, aboutItem, menuItem];


        $scope.selectLanguage = function (item) {

            $rootScope.language = item.code;
            localStorage.setItem("language", item.code);

            $rootScope.selectCategory("");
        }

        $scope.menuEnter = function () {

            menuItem.class = "selected";
            aboutItem.class = "";
            settingsItem.class = "";
            languageItem.class = "";
        }

        $scope.menuLeave = function () {

            menuItem.class = "";
            aboutItem.class = "ng-hide";
            settingsItem.class = "ng-hide";
            languageItem.class = "ng-hide";
            hideLanguages();
        }

        function showLanguages() {
            EN.class = "choice"
            RU.class = "choice";
        }

        function hideLanguages() {
            EN.class = "choice ng-hide"
            RU.class = "choice ng-hide";
        }

        $scope.menu = function (item) {

            if (item.title === "LANGUAGE") {

                if (item.class === "") {
                    aboutItem.class = "ng-hide";
                    settingsItem.class = "ng-hide";
                    languageItem.class = "selected";
                    showLanguages();
                } else {
                    aboutItem.class = "";
                    settingsItem.class = "";
                    languageItem.class = "";
                    hideLanguages();
                }
            }

            if (item.class === "choice") {
                $scope.selectLanguage(item);
            }
        }
    })



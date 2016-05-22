edflow

    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.otherwise("");

        $stateProvider

            .state("app", {
                url: "",
                //templateUrl: "assets/view/data/content.html",
                //controller: "edflowController as edflow"
            })

            .state("settings", {
                url: "/settings",
                templateUrl: "assets/view/data/content.html",
                controller: "settingsController as settings"
            })

            .state("wikipedia", {
                url: "/wikipedia",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

            .state("reddit", {
                url: "/reddit",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

            .state("lurkmore", {
                url: "/lurkmore",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

            .state("arxiv", {
                url: "/arxiv",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

        //$locationProvider.html5Mode(true);
    });

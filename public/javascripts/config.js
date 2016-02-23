edflow

    .config(function ($stateProvider, $urlRouterProvider, $locationProvider) {

        $urlRouterProvider.otherwise("");

        $stateProvider

            .state("app", {
                url: "",
                //templateUrl: "assets/view/data/content.html",
                //controller: "edflowController as edflow"
            })

            .state("news", {
                url: "/news",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("words", {
                url: "/words",
                templateUrl: "assets/view/data/words.html",
                controller: "wordsController as words"
            })

            .state("translate", {
                url: "/translate",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("wikipedia", {
                url: "/wikipedia",
                templateUrl: "assets/view/data/content.html",
                controller: "wikipediaController as wikipedia"
            })

            .state("book", {
                url: "/book",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

            .state("flow", {
                url: "/flow",
                templateUrl: "assets/view/data/content.html",
                controller: "contentController as content"
            })

        //$locationProvider.html5Mode(true);
    });
